package com.isekai.attendancecheck.database.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

import java.util.Date;

@Data
@DatabaseTable(tableName = "user_attendance")
public class UserAttendance {
    public UserAttendance() {}

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String userName;

    @ForeignCollectionField
    private ForeignCollection<UserMonthlyAttendance> userMonthlyAttendance;

    @DatabaseField(dataType = DataType.DATE)
    private Date lastLogin;

    @DatabaseField(dataType = DataType.DATE)
    private Date createAt;
    /*
    * 총 로그인 횟수
    */
    @DatabaseField
    private int loginCount;

    /*
    * 최근 연속 로그인 횟수
    * 처음이거나 도중에 끊기면 1임
    */
    @DatabaseField
    private int loginStack;


    /**
     * 최고 연속 로그인 횟수
     */
    @DatabaseField
    private int maxLoginStack;
}


