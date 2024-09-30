package com.isekai.attendancecheck.database;

import com.isekai.attendancecheck.Attendancecheck;
import com.isekai.attendancecheck.database.entity.UserAttendance;
import com.isekai.attendancecheck.database.entity.UserMonthlyAttendance;
import com.isekai.attendancecheck.util.DbFileUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static com.isekai.attendancecheck.Attendancecheck.LOGGER;

public class AttendanceDbContext {

    private Dao<UserAttendance, UUID> userAttendanceDao;
    private Dao<UserMonthlyAttendance,Integer> userMonthlyAttendanceDao;

    public AttendanceDbContext(ConnectionSource connectionSource){
        try {
            DaoManager.createDao(connectionSource, UserAttendance.class);
            DaoManager.createDao(connectionSource, UserMonthlyAttendance.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
