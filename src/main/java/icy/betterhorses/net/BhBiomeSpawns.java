package icy.betterhorses.net;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;

public final class BhBiomeSpawns {

    static final TagKey<Biome> SPAWNS = TagKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(IcysBetterHorses.MOD_ID, "spawns_horses"));

    private BhBiomeSpawns() {}

    public static void register() {
        BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(IcysBetterHorses.MOD_ID, "horse_biome_spawns"))
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.tag(SPAWNS), (selectionContext, context) -> {
                    BhTuning tuning = BhConfig.tuning();
                    if (tuning.spawnWeight() <= 0) {
                        return;
                    }
                    MobSpawnSettings mobSettings = selectionContext.getBiome().getMobSettings();
                    boolean alreadyHasHorse = mobSettings.getMobs(MobCategory.CREATURE).unwrap().stream()
                            .anyMatch(weighted -> weighted.type == EntityType.HORSE);
                    float floor = (float) tuning.spawnFloor();
                    boolean boostedProbability = !alreadyHasHorse
                            && mobSettings.getCreatureProbability() < floor;

                    if (!alreadyHasHorse) {
                        context.getSpawnSettings().addSpawn(
                                MobCategory.CREATURE,
                                new MobSpawnSettings.SpawnerData(
                                        EntityType.HORSE, tuning.spawnWeight(), tuning.groupMin(), tuning.groupMax()));
                    }

                    if (boostedProbability) {
                        context.getSpawnSettings().setCreatureSpawnProbability(floor);
                    }
                });
    }
}
