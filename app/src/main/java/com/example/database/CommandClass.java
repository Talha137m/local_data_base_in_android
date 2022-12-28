package com.example.database;

import androidx.annotation.NonNull;

public class CommandClass {
    public static final String TABLE_NAME="Student";
    public static final String COLUMN_NAME="Name";
    public static final String COLUMN_ROLL_NO="RollNo";
    public static final String COLUMN_FEE="Fee";
    //Command of create table

    public static final String CREATE_TABLE=String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY , %s TEXT ,%s REAL ) ",TABLE_NAME,COLUMN_ROLL_NO
            ,COLUMN_NAME,COLUMN_FEE);
    public static final String DROP_TABLE="DROP TABLE " + TABLE_NAME;
    public static final String SELECT_ALL_STUDENT = "SELECT * FROM "+TABLE_NAME;

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
