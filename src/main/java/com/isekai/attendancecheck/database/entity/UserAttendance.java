package com.isekai.attendancecheck.database.entity;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "user_attendance")
public class UserAttendance {
    UserAttendance() {}
    @DatabaseField(generatedId = false)
    private UUID id;

    @DatabaseField
    private String userName;



}
