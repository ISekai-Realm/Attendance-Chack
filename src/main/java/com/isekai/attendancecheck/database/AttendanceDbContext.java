package com.isekai.attendancecheck.database;

import com.isekai.attendancecheck.database.entity.UserAttendance;
import com.isekai.attendancecheck.database.entity.UserMonthlyAttendance;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.support.ConnectionSource;

import net.minecraft.server.network.ServerPlayerEntity;

import java.sql.SQLException;
import java.util.*;

import static com.isekai.attendancecheck.Attendancecheck.LOGGER;

public class AttendanceDbContext {
    private final ConnectionSource connectionSource;
    private final Dao<UserAttendance, String> userAttendanceDao;
    private final Dao<UserMonthlyAttendance, Integer> userMonthlyAttendanceDao;

    public AttendanceDbContext(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        try {
            userAttendanceDao = DaoManager.createDao(connectionSource, UserAttendance.class);
            userMonthlyAttendanceDao = DaoManager.createDao(connectionSource, UserMonthlyAttendance.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createUser(ServerPlayerEntity player) {
        return createUser(player.getUuid(), player.getName().getString());
    }

    public boolean createUser(UUID playerUUID, String playerName) {
        // 유저가 이미 있는지 확인
        var user = fetchUser(playerUUID);
        if (user.isPresent()) {
            LOGGER.info("User already exists", playerName);
            return false;
        }

        // 유저 생성
        var createDate = new Date();
        var uuidAsString = playerUUID.toString();
        try {
            var newUser = UserAttendance.builder()
                    .id(playerUUID.toString())
                    .userName(playerName)
                    .createAt(createDate)
                    .lastLogin(createDate)
                    .loginCount(1)
                    .loginStack(1)
                    .totalLoginCount(1)
                    .maxLoginStack(1)
                    .build();

            userAttendanceDao.create(newUser);

            var userMonth = UserMonthlyAttendance.builder()
                    .userAttendance(newUser)
                    .month(createDate.getMonth())
                    .count(1)
                    .build();

            userMonthlyAttendanceDao.create(userMonth);
        } catch (SQLException e) {
            LOGGER.error("Failed to create user", e);
            return false;
        }
        return true;
    }

    public boolean updateUser(UUID playerUUID, int count) {
        var _user = fetchUser(playerUUID);
        if (_user.isEmpty()) {
            return false;
        }

        var user = _user.get();
        var today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        var todayMidnight = calendar.getTime();
        calendar.set(Calendar.DAY_OF_YEAR, -1);
        var yesterdayMidnight = calendar.getTime();

        //어제 접속 했는가
        var isYesterdayLogin = user.getLastLogin().after(yesterdayMidnight) && user.getLastLogin().before(todayMidnight);
        //오늘 접속 했는가?
        var isTodayLogin = user.getLastLogin().after(todayMidnight);
        //월 초인가?
        var isMonthStart = user.getLastLogin().getMonth() != today.getMonth();
        try {
            //오늘 접속 안했다면?
            if(!isTodayLogin){
                if(isMonthStart){
                    user.setLoginCount(1);
                }else {
                    user.setLoginCount(user.getLoginCount() + count);
                }

                user.setTotalLoginCount(user.getTotalLoginCount() + count);
                if(isYesterdayLogin) {
                    user.setLoginStack(user.getLoginStack() + count);
                    user.setMaxLoginStack(Math.max(user.getMaxLoginStack(), user.getLoginStack()));
                } else{
                    user.setLoginStack(1);
                }
            }

            user.setLastLogin(today);
            userAttendanceDao.update(user);

            // 월별 출석 업데이트
            var userMonthlyAttendance = userMonthlyAttendanceDao.queryBuilder()
                    .where()
                    .eq("userAttendance_id", user.getId())
                    .and()
                    .eq("month", today.getMonth())
                    .queryForFirst();

            if (userMonthlyAttendance == null) {
                userMonthlyAttendance = UserMonthlyAttendance.builder()
                        .userAttendance(user)
                        .month(today.getMonth())
                        .count(count)
                        .build();
                userMonthlyAttendanceDao.create(userMonthlyAttendance);
            } else {
                if(!isTodayLogin){
                    userMonthlyAttendance.setCount(userMonthlyAttendance.getCount() + count);
                    userMonthlyAttendanceDao.update(userMonthlyAttendance);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update user", e);
            return false;
        }

        return true;
    }

    public boolean updateUserForce(UUID playerUUID, String playerName) {
        return true;
    }

    public boolean deleteUser(UUID playerUUID) {
        try {
            // TODO: delete monthly attendance
            userAttendanceDao.deleteById(playerUUID.toString());
            return true;
        } catch (SQLException e) {
            LOGGER.error("Failed to delete user", e);
            return false;
        }
    }

    public Optional<UserAttendance> fetchUser(UUID playerUUID) {
        try {
            var user = userAttendanceDao.queryForId(playerUUID.toString());
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            LOGGER.error("Failed to fetch user", e);
            return Optional.empty();
        }
    }

    public List<UserAttendance> fetchUsers() {
        try {
            return userAttendanceDao.queryForAll();
        } catch (SQLException e) {
            LOGGER.error("Failed to fetch users", e);
        }
        return List.of();
    }

    public UserMonthlyAttendance fetchUserMonthly(String playerUUID,int month){
        try {
            var user = userAttendanceDao.queryForId(playerUUID);
            if(user == null){
                return null;
            }
            return userMonthlyAttendanceDao.queryBuilder()
                    .where()
                    .eq("userAttendance_id", user.getId())
                    .and()
                    .eq("month", month)
                    .queryForFirst();
        } catch (SQLException e) {
            LOGGER.error("Failed to fetch user monthly", e);
            return null;
        }
    }
}
