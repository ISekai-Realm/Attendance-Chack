package com.isekai.attendancecheck.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public interface AttendanceEvent {
    ///출석 할때마다 일어나는 이벤트
    Event<AttendanceEvent> USER_ATTENDANCE = EventFactory.createArrayBacked(AttendanceEvent.class,
            (listeners) -> (player, count) -> {
                for (AttendanceEvent listener : listeners) {
                    listener.onUserAttendanceEvent(player, count);
                }
            }
    );

    ///연속 출석 이벤트
    Event<AttendanceEvent> USER_STACK_ATTENDANCE = EventFactory.createArrayBacked(AttendanceEvent.class,
            (listeners) -> (player, count) -> {
                for (AttendanceEvent listener : listeners) {
                    listener.onUserAttendanceEvent(player, count);
                }
            }
    );
    ///한달 동안 총 출석 횟수 이벤트
    Event<AttendanceEvent> USER_MONTHLY_TOTAL_ATTENDANCE = EventFactory.createArrayBacked(AttendanceEvent.class,
            (listeners) -> (player, count) -> {
                for (AttendanceEvent listener : listeners) {
                    listener.onUserAttendanceEvent(player, count);
                }
            }
    );

    ///현재 까지 총 출석 이벤트
    Event<AttendanceEvent> USER_TOTAL_ATTENDANCE = EventFactory.createArrayBacked(AttendanceEvent.class,
            (listeners) -> (player, count) -> {
                for (AttendanceEvent listener : listeners) {
                    listener.onUserAttendanceEvent(player, count);
                }
            }
    );

    /// 출석 이벤트
    /// # Parameters
    /// - `player`: 출석한 플레이어
    /// - `count`: 출석 횟수
    void onUserAttendanceEvent(ServerPlayerEntity player,int count);

}
