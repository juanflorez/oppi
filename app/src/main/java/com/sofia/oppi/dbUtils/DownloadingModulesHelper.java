package com.sofia.oppi.dbUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by juanflorez on 08/04/15.
 */
public class DownloadingModulesHelper extends SQLiteOpenHelper {
    private final String TAG = "DownloadDbHelper";

    public DownloadingModulesHelper(Context context) {
        super(context, DbModules.DB_NAME, null, DbModules.DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DbModules.CREATE_DOWNLOADS_TABLE);
        Log.i(TAG,"DB Created");
    }

//Not in use yet
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DbModules.DELETE_DOWNLOADS_TABLE);
        onCreate(db);
    }
}