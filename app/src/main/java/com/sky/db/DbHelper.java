package com.sky.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sky.util.Constant;

public class DbHelper extends SQLiteOpenHelper {

    private static int version = 1;

    public DbHelper(Context context) {
        super(new DatabaseContext(context), Constant.DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void close(SQLiteDatabase database) {
        // TODO Auto-generated method stub
        if (database != null) {
            database.close();
        }
    }

}
