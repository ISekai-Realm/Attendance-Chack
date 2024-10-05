package com.isekai.attendancecheck.event;

import com.isekai.attendancecheck.database.AttendanceDbContext;
import com.j256.ormlite.support.ConnectionSource;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Date;

import static com.isekai.attendancecheck.Attendancecheck.LOGGER;
public final class ServerEvent {
    private static AttendanceDbContext dbContext;
    public static void Init(ConnectionSource connectionSource) {
        dbContext = new AttendanceDbContext(connectionSource);
        var instance = new ServerEvent();
        ServerPlayConnectionEvents.JOIN.register(instance::onJoin);
        ServerPlayConnectionEvents.DISCONNECT.register(instance::onDisconnect);
        AttendanceEvent.USER_MONTHLY_TOTAL_ATTENDANCE.register(instance::onUserMonthlyTotalAttendance);
        LOGGER.info("ServerEvent initialized");
    }

    private void onUserMonthlyTotalAttendance(ServerPlayerEntity player, int count) {
        LOGGER.info("user monthly total attendance event [%s]: %d".formatted(player.getName().getString(), count));
    }

    private void onDisconnect(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer minecraftServer) {

    }

    private void onJoin(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {
        var player = serverPlayNetworkHandler.player;
        var _user = dbContext.fetchUser(player.getUuid());
        if(_user.isPresent()){
            LOGGER.info("user already exists");
            dbContext.updateUser(player.getUuid(), 1);

            //흠 그렇게 좋은 코드는 아닌듯?
            var user = _user.get();
            var user_monthly = dbContext.fetchUserMonthly(user.getId(), new Date().getMonth());

            AttendanceEvent.USER_ATTENDANCE.invoker().onUserAttendanceEvent(player,user.getLoginCount());
            AttendanceEvent.USER_STACK_ATTENDANCE.invoker().onUserAttendanceEvent(player,user.getLoginStack());
            AttendanceEvent.USER_TOTAL_ATTENDANCE.invoker().onUserAttendanceEvent(player,user.getTotalLoginCount());
            AttendanceEvent.USER_MONTHLY_TOTAL_ATTENDANCE.invoker().onUserAttendanceEvent(player, user_monthly.getCount());
        }else{
            dbContext.createUser(player.getUuid(), player.getName().getString());
            LOGGER.info("new user created");
        }
    }


}
