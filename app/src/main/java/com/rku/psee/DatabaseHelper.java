package com.rku.psee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PSEE.db";
    public static final String TABLE_NAME = "student";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "YEAR";
    public static final String COL_4 = "COLOR";
    public static final String COL_5 = "PANTONE_VALUE";
    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,1);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create TABLE " + TABLE_NAME + "(" + COL_1 + " integer primary key," +
                COL_2 + " text," +
                COL_3 + " integer," +
                COL_4 + " text," +
                COL_5 + " text" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }
    public long insert(String name,int year, String color, String pantone_value){
        long result=0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor c = db.query(TABLE_NAME,
                null, null,
                null, null, null, null, null);
        if(c == null) {
            values.put(COL_2, name);
            values.put(COL_3, year);
            values.put(COL_4, color);
            values.put(COL_5, pantone_value);

            result = db.insert(TABLE_NAME, null, values);
        }
        return result;
    }

    public JSONArray getData() {
        String sql = "select  *from " + TABLE_NAME;
        Log.e("sql", sql);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        Log.e("jsonArray", String.valueOf(resultSet));
        return resultSet;
    }
}
