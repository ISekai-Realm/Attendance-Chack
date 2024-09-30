package com.isekai.attendancecheck.util;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.function.Function;

import static com.isekai.attendancecheck.Attendancecheck.LOGGER;

public class DbFileUtils {
    private String dbName;
    Path dbFolderPath;

    public DbFileUtils(String dbName) {
        this.dbName = dbName;
        this.dbFolderPath = FabricLoader.getInstance().getGameDir().resolve("db");
    }
    /**
     * Create the DB folder if it doesn't exist
     *
     * @return true if the folder was created, false if it already exists
     */
    private boolean makeDbFolder() {
        if (dbFolderPath.toFile().mkdir()) {
            LOGGER.info("Attendance DB folder created");
            return true;
        } else {
            LOGGER.info("Attendance DB folder already exists");
            return false;
        }
    }

    public void makeDbFile() {
        File dbFile = getDbFile();

        if (makeDbFolder()) {
            if (!dbFile.exists()) {
                try {
                    dbFile.createNewFile();
                    LOGGER.info("Attendance DB file created");
                } catch (IOException e) {
                    LOGGER.error("Failed to create Attendance  DB file", e);
                }
            } else {
                LOGGER.info("Attendance DB file already exists");
            }
        }
    }

    public File getDbFile() {
        return dbFolderPath.resolve(dbName).toFile();
    }

    private void willDbFileCreated() {

    }

    private void didDbFileCreated() {

    }


}
