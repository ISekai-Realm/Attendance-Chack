package com.isekai.attendancecheck.database;

import com.isekai.attendancecheck.database.entity.UserAttendance;
import com.isekai.attendancecheck.database.entity.UserMonthlyAttendance;
import com.isekai.attendancecheck.util.DbFileUtils;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;

import java.sql.SQLException;

public class DatabaseManager {
    private static final String dbFileName = "attendance.db";
    private static final DbFileUtils utils = new DbFileUtils(dbFileName);

    @Getter
    private ConnectionSource connectionSource;

    public DatabaseManager() {
    }

    public void initializeDatabase() {
        utils.makeDbFile();
        try {
            connectionSource = new JdbcConnectionSource(connectionURL());

            TableUtils.createTableIfNotExists(connectionSource, UserAttendance.class);
            TableUtils.createTableIfNotExists(connectionSource, UserMonthlyAttendance.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private String connectionURL() {
        return "jdbc:sqlite:" + utils.getDbFile().getAbsolutePath();
    }


}
