package com.example.fitnesstrackingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String databaseName = "Lifts.db";
    private static final String tableName = "Lifts";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseHelper.databaseName, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase  db) {
        db.execSQL("CREATE TABLE "+DatabaseHelper.tableName+" (LiftName TEXT PRIMARY KEY, Weight INTEGER NOT NULL, Reps INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DatabaseHelper.tableName);
        onCreate(db);
    }

}
