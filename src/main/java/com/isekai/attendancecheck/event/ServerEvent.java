package com.isekai.attendancecheck.event;

import com.isekai.attendancecheck.database.AttendanceDbContext;
import com.j256.ormlite.support.ConnectionSource;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import static com.isekai.attendancecheck.Attendancecheck.LOGGER;
public final class ServerEvent {
    private static AttendanceDbContext dbContext;
    public static void Init(ConnectionSource connectionSource) {
        dbContext = new AttendanceDbContext(connectionSource);
        var instance = new ServerEvent();
        ServerPlayConnectionEvents.JOIN.register(instance::onJoin);
        ServerPlayConnectionEvents.DISCONNECT.register(instance::onDisconnect);
        LOGGER.info("ServerEvent initialized");
    }

    private void onDisconnect(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer minecraftServer) {

    }

    private void onJoin(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {
        var player = serverPlayNetworkHandler.player;
        dbContext.createUser(player.getUuid(), player.getName().getString());
        LOGGER.info("Player joined");
    }


}
