package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyHelper extends SQLiteOpenHelper {


    public static final String DATA_BASE_NAME = "studentDataBase";
    Context context;

    public static final int VERSION = 2;
    public static MyHelper instance;

    private MyHelper(@Nullable Context context) {

        super(context, DATA_BASE_NAME, null, VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CommandClass.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL(CommandClass.DROP_TABLE);
            onCreate(sqLiteDatabase);
        }

    }

    public static MyHelper getHelperClassObject(Context context) {

            if (instance== null) {
                instance = new MyHelper(context);
            }
            return instance;
        }
    boolean insertData(@NonNull ModelClass modelClass) {
        SQLiteDatabase myDataBase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CommandClass.COLUMN_NAME, modelClass.getName());
        contentValues.put(CommandClass.COLUMN_ROLL_NO, modelClass.getRollNo());
        contentValues.put(CommandClass.COLUMN_FEE, modelClass.getFee());

        long effectedRows = myDataBase.insert(CommandClass.TABLE_NAME, null,
                contentValues);
        if (effectedRows >= 0) {
            context.sendBroadcast(new Intent(MainActivity.MY_ACTION));
            return true;
        }
        return false;

    }

    List<ModelClass> fetchData() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        List<ModelClass> modelClassList;
        try (Cursor cursor = sqLiteDatabase.rawQuery(CommandClass.SELECT_ALL_STUDENT, null)) {
            {
                modelClassList = new ArrayList<>(cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        ModelClass modelClass = new ModelClass();
                        int index = cursor.getColumnIndex(CommandClass.COLUMN_NAME);
                        modelClass.setName(cursor.getString(index));
                        index = cursor.getColumnIndex(CommandClass.COLUMN_FEE);
                        modelClass.setFee(cursor.getFloat(index));
                        index = cursor.getColumnIndex(CommandClass.COLUMN_ROLL_NO);
                        modelClass.setRollNo(cursor.getInt(index));
                        modelClassList.add(modelClass);
                    } while (cursor.moveToNext());
                }
            }
        }
        return modelClassList;
    }

    boolean updateStudent(ModelClass modelClass) {
        SQLiteDatabase updateDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CommandClass.COLUMN_NAME, modelClass.getName());
        contentValues.put(CommandClass.COLUMN_ROLL_NO, modelClass.getRollNo());
        contentValues.put(CommandClass.COLUMN_FEE, modelClass.getFee());
        long effectedRows = updateDatabase.update(CommandClass.TABLE_NAME, contentValues, CommandClass.COLUMN_ROLL_NO + "=?"
                , new String[]{String.valueOf(modelClass.getRollNo())});

        if (effectedRows >= 0) {
            context.sendBroadcast(new Intent(MainActivity.MY_ACTION));
            return true;
        }
        return false;
    }
    public boolean deleteDta(ModelClass modelClass){
        SQLiteDatabase deleteData=getWritableDatabase();
        long effectedRows=deleteData.delete(CommandClass.TABLE_NAME,CommandClass.COLUMN_ROLL_NO + "=?",
                new String[]{String.valueOf(modelClass.getRollNo())});
        if (effectedRows>0){
            context.sendBroadcast(new Intent(MainActivity.MY_DELETE_ACTION));
            return true;
        }
        return false;
    }

}

