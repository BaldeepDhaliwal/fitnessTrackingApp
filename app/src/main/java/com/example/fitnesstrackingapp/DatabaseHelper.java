package com.example.fitnesstrackingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String databaseName = "Lifts.db";
    private static final String tableName = "Lifts";
//    private static final String squat = "Back Squat";
//    private static final String deadlift = "Deadlift";
//    private static final String bench = "Bench Press";
//    private static final String ohp = "Overhead Press";
//    private static final String rows = "Barbell Rows";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseHelper.databaseName, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();
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

//    public void initializeData(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("INSERT INTO Lifts (LiftName,Weight,Reps) VALUES (\"Back Squat\",0,0), (\"Deadlift\", 0, 0),(\"Bench Press\", 0, 0),(\"Overhead Press\", 0, 0),(\"Barbell Rows\", 0, 0)");
//    }

    //update weight for particular lift
    public void setWeight(String liftName, Integer weight){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update Lifts set Weight="+weight+" where LiftName ==\""+liftName+"\"");

    }

    //update numREps for particular lift
    public void setReps(String liftName, Integer reps){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update Lifts set Reps="+reps+" where LiftName ==\""+liftName+"\"");

    }

}
