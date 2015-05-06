package com.sofia.oppi.dbUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by juanflorez on 08/04/15.
 */
public class InstalledModulesHelper extends SQLiteOpenHelper {

    public static String TAG = "InstalledModDBHelper";
    public InstalledModulesHelper(Context context) {
        super(context, DbModules.DB_NAME, null, DbModules.DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbModules.CREATE_MODULES_TABLE);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Taken from Android website example
        db.execSQL(DbModules.DELETE_MODULES_TABLE);

        onCreate(db);
    }
    /**
     * Returns a Cursor with the data to display locally available modules
     * */
    public Cursor getDisplayableModules (SQLiteDatabase db){

        Cursor cursor = db.rawQuery(DbModules.GET_DISP_MODULES, null);
        Log.d(TAG, "Total Records: "+cursor.getCount());
        return cursor;
    }



 }