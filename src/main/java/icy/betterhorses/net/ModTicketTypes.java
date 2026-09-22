package icy.betterhorses.net;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.TicketType;

public final class ModTicketTypes {

    public static final TicketType HORSE_TASK = register("horse_task", new TicketType(
            200L,
            TicketType.FLAG_LOADING | TicketType.FLAG_SIMULATION | TicketType.FLAG_KEEP_DIMENSION_ACTIVE));

    public static void init() {
    }

    private static TicketType register(String path, TicketType type) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(IcysBetterHorses.MOD_ID, path);
        return Registry.register(BuiltInRegistries.TICKET_TYPE, id, type);
    }

    private ModTicketTypes() {}
}
