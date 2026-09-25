package icy.betterhorses.net;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

/**
 * Tops up wild horses around each player on a timer, independent of the vanilla
 * creature mob cap and spawn weights, so other mods' animals cannot crowd horses out.
 * Each attempt picks one chunk between the configured minimum and maximum distance
 * from the player and only spawns there while the chunk is under its horse cap.
 */
public final class BhHorseSpawner {

    private static final int GROUP_SCATTER = 4;

    private BhHorseSpawner() {}

    public static void tick(MinecraftServer server) {
        BhSpawnerSettings settings = BhConfig.spawner();
        if (!settings.enabled() || server.getTickCount() % settings.checkIntervalTicks() != 0) {
            return;
        }
        for (ServerLevel level : server.getAllLevels()) {
            for (ServerPlayer player : level.players()) {
                if (!player.isSpectator()) {
                    trySpawnNear(level, player.blockPosition(), player.getGameProfile().getName(), settings);
                }
            }
        }
    }

    static void trySpawnNear(ServerLevel level, BlockPos center, String who, BhSpawnerSettings settings) {
        RandomSource random = level.getRandom();
        if (random.nextDouble() >= settings.chance()) {
            return;
        }

        int nearRoom = settings.maxNearPlayer() - countWildHorsesNear(level, center, settings.maxDistance());
        if (nearRoom <= 0) {
            return;
        }

        double angle = random.nextDouble() * Math.PI * 2.0D;
        double distance = settings.minDistance()
                + random.nextDouble() * (settings.maxDistance() - settings.minDistance());
        int x = center.getX() + (int) Math.round(Math.cos(angle) * distance);
        int z = center.getZ() + (int) Math.round(Math.sin(angle) * distance);

        ChunkPos chunk = new ChunkPos(new BlockPos(x, 0, z));
        int room = settings.maxPerChunk() - countWildHorses(level, chunk);
        if (room <= 0) {
            return;
        }

        BlockPos origin = surface(level, x, z, settings);
        if (origin == null || !level.getBiome(origin).is(BhBiomeSpawns.SPAWNS)) {
            return;
        }

        BhTuning tuning = BhConfig.tuning();
        int wanted = tuning.groupMin() + random.nextInt(tuning.groupMax() - tuning.groupMin() + 1);
        int count = Math.min(wanted, Math.min(room, nearRoom));

        SpawnGroupData groupData = null;
        int spawned = 0;
        for (int i = 0; i < count; i++) {
            BlockPos pos = i == 0
                    ? origin
                    : surface(level, clampToChunk(x + random.nextInt(GROUP_SCATTER * 2 + 1) - GROUP_SCATTER, chunk.getMinBlockX()),
                            clampToChunk(z + random.nextInt(GROUP_SCATTER * 2 + 1) - GROUP_SCATTER, chunk.getMinBlockZ()),
                            settings);
            if (pos == null) {
                continue;
            }
            Horse horse = EntityType.HORSE.create(level);
            if (horse == null) {
                continue;
            }
            horse.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, random.nextFloat() * 360.0F, 0.0F);
            if (!horse.checkSpawnObstruction(level)) {
                horse.discard();
                continue;
            }
            groupData = horse.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, groupData);
            level.addFreshEntityWithPassengers(horse);
            spawned++;
        }
        if (spawned > 0) {
            IcysBetterHorses.LOGGER.debug("[HORSE_SPAWNER] spawned {} near {} in chunk {}", spawned, who, chunk);
        }
    }

    private static int clampToChunk(int value, int chunkMin) {
        return Math.max(chunkMin, Math.min(chunkMin + 15, value));
    }

    private static BlockPos surface(ServerLevel level, int x, int z, BhSpawnerSettings settings) {
        if (!level.isPositionEntityTicking(new BlockPos(x, level.getMinBuildHeight(), z))) {
            return null;
        }
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        BlockPos pos = new BlockPos(x, y, z);
        if (!level.getFluidState(pos).isEmpty()
                || !level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                || level.getNearestPlayer(x, y, z, settings.minDistance(), false) != null
                || !BhHorseSpawnRules.checkHorseGroundRules(level, MobSpawnType.NATURAL, pos)) {
            return null;
        }
        return pos;
    }

    private static int countWildHorsesNear(ServerLevel level, BlockPos center, int radius) {
        AABB box = new AABB(center).inflate(radius, level.getHeight(), radius);
        return level.getEntitiesOfClass(Horse.class, box, BhHorseSpawner::isWild).size();
    }

    private static boolean isWild(Horse horse) {
        return !horse.isTamed() && !horse.hasCustomName() && !IHorseData.of(horse).bh_isOwned();
    }

    private static int countWildHorses(ServerLevel level, ChunkPos chunk) {
        AABB column = new AABB(chunk.getMinBlockX(), level.getMinBuildHeight(), chunk.getMinBlockZ(),
                chunk.getMaxBlockX() + 1, level.getMaxBuildHeight(), chunk.getMaxBlockZ() + 1);
        return level.getEntitiesOfClass(Horse.class, column, BhHorseSpawner::isWild).size();
    }
}
