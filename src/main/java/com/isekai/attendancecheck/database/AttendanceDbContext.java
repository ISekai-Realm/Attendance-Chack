package com.isekai.attendancecheck.database;

import com.isekai.attendancecheck.database.entity.UserAttendance;
import com.isekai.attendancecheck.database.entity.UserMonthlyAttendance;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.support.ConnectionSource;

import net.minecraft.server.network.ServerPlayerEntity;


import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.isekai.attendancecheck.Attendancecheck.LOGGER;

public class AttendanceDbContext {
    private final ConnectionSource connectionSource;
    private final Dao<UserAttendance, String> userAttendanceDao;
    private final Dao<UserMonthlyAttendance,Integer> userMonthlyAttendanceDao;

    public AttendanceDbContext(ConnectionSource connectionSource){
        this.connectionSource = connectionSource;
        try {
            userAttendanceDao = DaoManager.createDao(connectionSource, UserAttendance.class);
            userMonthlyAttendanceDao = DaoManager.createDao(connectionSource, UserMonthlyAttendance.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createUser(ServerPlayerEntity player){
        return createUser(player.getUuid(), player.getName().getString());
    }

    public boolean createUser(UUID playerUUID, String playerName) {
        //유저가 이미 있는지 확인
        var user = fetchUser(playerUUID);
        if(user.isPresent()){
            LOGGER.info("User already exists", playerName);
            return false;
        }

        //유저 생성
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

    public boolean updateUser() {

        return true;
    }

    public boolean updateUserForce(UUID playerUUID, String playerName) {
        return true;
    }

    public boolean deleteUser(UUID playerUUID) {
        try {
            //TODO: delete monthly attendance
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
            return Optional.of(user);
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
}
