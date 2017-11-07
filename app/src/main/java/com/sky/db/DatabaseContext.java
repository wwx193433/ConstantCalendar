package com.sky.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 17-10-11.
 */
class DatabaseContext extends ContextWrapper {

    private static final String DEBUG_CONTEXT = "DatabaseContext";

    public DatabaseContext(Context base) {
        super(base);
    }

    @Override
    public File getDatabasePath(String DB_NAME) {
        String dbPath = "/data/data/" + this.getPackageName()
                + "/databases/" + DB_NAME;
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            try {
                FileOutputStream out = new FileOutputStream(dbPath);
                InputStream in = this.getAssets().open(DB_NAME);
                byte[] buffer = new byte[1024];
                int readBytes = 0;
                while ((readBytes = in.read(buffer)) != -1)
                    out.write(buffer, 0, readBytes);
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dbFile;
    }

    /*
     * this version is called for android devices >= api-11. thank to @damccull
     * for fixing this.
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        return openOrCreateDatabase(name, mode, factory);
    }

    /* this version is called for android devices < api-11 */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
                getDatabasePath(name), null);

        return result;
    }
}