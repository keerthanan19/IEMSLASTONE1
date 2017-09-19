package com.example.hamshi.iems;

/**
 * Created by hamshi on 19-06-2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ProgrammingKnowledge on 4/3/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Health.db";

    public static final String TABLE_NAME = "User";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "PASSWORD";

    public static final String TABLE_body = "List";
    public static final String COL_ID = "BID";
    public static final String COL_SuBbody = "SUBBODY";
    public static final String COL_Symptoms = "SYMPTOM";
    public static final String COL_Disease = "DISEASE";


    public SQLiteDatabase db;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,PASSWORD TEXT)");
        db.execSQL("create table " + TABLE_body +" (BID INTEGER PRIMARY KEY AUTOINCREMENT,SUBBODY TEXT,SYMPTOM TEXT,DISEASE TEXT)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_body);

        onCreate(db);
    }

    public boolean insertData(String name,String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,password);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public String getSinlgeEntry(String userName) {
        Cursor cursor = db.query("LOGIN", null, " USERNAME=?",
                new String[] { userName }, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;
    }


    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =db.rawQuery("select * from"+TABLE_NAME,null);
        return res;
    }
}