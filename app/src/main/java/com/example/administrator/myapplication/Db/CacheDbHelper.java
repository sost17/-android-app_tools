package com.example.administrator.myapplication.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 16-3-20.
 */
public class CacheDbHelper extends SQLiteOpenHelper {
    public CacheDbHelper(Context context, int version) {
        super(context, "cache.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists CacheList (id INTEGER primary key autoincrement,date INTEGER unique,json text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
