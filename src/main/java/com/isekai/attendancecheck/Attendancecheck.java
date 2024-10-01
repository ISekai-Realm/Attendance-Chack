package com.isekai.attendancecheck;

import com.isekai.attendancecheck.database.AttendanceDbContext;
import com.isekai.attendancecheck.database.DatabaseManager;
import com.isekai.attendancecheck.event.ServerEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class Attendancecheck implements ModInitializer {
    public static final String MOD_ID = "attendancecheck";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        var db = new DatabaseManager();
        db.initializeDatabase();
        ServerEvent.Init(db.getConnectionSource());


    }



}
