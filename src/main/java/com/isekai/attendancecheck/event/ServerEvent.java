package com.isekai.attendancecheck.event;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import static com.isekai.attendancecheck.Attendancecheck.LOGGER;
public final class ServerEvent {
    public static void Init() {
        var instance = new ServerEvent();
        ServerPlayConnectionEvents.JOIN.register(instance::onJoin);
        ServerPlayConnectionEvents.DISCONNECT.register(instance::onDisconnect);
        LOGGER.info("ServerEvent initialized");
    }

    private void onDisconnect(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer minecraftServer) {

    }

    private void onJoin(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {

        LOGGER.info("Player joined");
    }


}
