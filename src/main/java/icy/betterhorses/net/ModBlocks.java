package icy.betterhorses.net;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class ModBlocks {

    public static final icy.betterhorses.net.item.HearthlightBlock HEARTHLIGHT =
            Registry.register(BuiltInRegistries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(IcysBetterHorses.MOD_ID, "hearthlight"),
                    new icy.betterhorses.net.item.HearthlightBlock(BlockBehaviour.Properties.of()
                            .noCollission().noOcclusion().replaceable().noLootTable().lightLevel(state -> 10)));

    public static void init() {
    }

    private ModBlocks() {}
}
