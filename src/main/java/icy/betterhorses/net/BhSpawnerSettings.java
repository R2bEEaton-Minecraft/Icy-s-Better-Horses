package icy.betterhorses.net;

import net.minecraft.util.Mth;

public record BhSpawnerSettings(
        boolean enabled,
        int checkIntervalTicks,
        double chance,
        int minDistance,
        int maxDistance,
        int maxPerChunk) {

    public static BhSpawnerSettings defaults() {
        return new BhSpawnerSettings(true, 100, 0.5D, 32, 48, 1);
    }

    public BhSpawnerSettings clamped() {
        int min = Mth.clamp(minDistance, 16, 256);
        return new BhSpawnerSettings(
                enabled,
                Mth.clamp(checkIntervalTicks, 1, 72000),
                Mth.clamp(chance, 0.0D, 1.0D),
                min,
                Mth.clamp(maxDistance, min, 512),
                Mth.clamp(maxPerChunk, 1, 16));
    }
}
