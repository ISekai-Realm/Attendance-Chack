package com.isekai.attendancecheck.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

import java.util.UUID;

@Data
@DatabaseTable(tableName = "user_monthly_attendance")
public class UserMonthlyAttendance {
    public UserMonthlyAttendance() {}

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
    private UserAttendance userAttendance;
    /**
     * 해당 월
     */
    @DatabaseField
    private int month;

    /*
     * 해당 월 로그인 횟수
     */
    @DatabaseField
    private int count;
}
