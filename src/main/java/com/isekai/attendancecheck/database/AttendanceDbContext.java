package com.isekai.attendancecheck.database;

import com.isekai.attendancecheck.Attendancecheck;
import com.isekai.attendancecheck.util.DbFileUtils;
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

import static com.isekai.attendancecheck.Attendancecheck.LOGGER;

public class AttendanceDbContext {
    private Connection connection;
    private DbFileUtils fileUtils;
    public AttendanceDbContext() {
        String dbFileName = "attendance.db";
        this.fileUtils = new DbFileUtils(dbFileName);
    }

    public void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");

            fileUtils.makeDbFile();
        connect();

        Statement stmt = connection.createStatement();
            stmt.executeUpdate(createTableQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try{
            connection = DriverManager.getConnection(connectionURL());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private String connectionURL() {
        return "jdbc:sqlite:" + fileUtils.getDbFile().getAbsolutePath();
    }

    private String createTableQuery() {
        return "CREATE TABLE IF NOT EXISTS attendance (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "player_uuid TEXT NOT NULL," +
                    "last_login DATE NOT NULL," +
                    "login_stack INTEGER NOT NULL," +
                    "max_login_stack INTEGER NOT NULL" +
                ");";

    }
}
