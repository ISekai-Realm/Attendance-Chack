package com.isekai.attendancecheck;

import com.isekai.attendancecheck.database.AttendanceDbContext;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class Attendancecheck implements ModInitializer {
    public static final String MOD_ID = "attendancecheck";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        var db = new AttendanceDbContext();
        db.initializeDatabase();

    }
}
