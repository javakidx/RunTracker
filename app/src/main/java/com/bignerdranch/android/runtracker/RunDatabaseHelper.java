package com.bignerdranch.android.runtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 安軻 on 2015/8/26.
 */
public class RunDatabaseHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "run.sqlite";
    private static final int VERSION = 1;

    private static final String TABLE_NAME = "run";
    private static final String COLUMN_RUN_START_DATE = "start_date";

    public RunDatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table run(_id integer primary key autoincrement, start_date integer)");
        db.execSQL("create table location(" +
                "timestamp integer, latitude real, longtitude real, altitude real," +
                " provider varchar(100), run_id integer reference run(_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public long insertRun(Run run)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_RUN_START_DATE, run.getStartDate().getTime());

        return getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }
}
