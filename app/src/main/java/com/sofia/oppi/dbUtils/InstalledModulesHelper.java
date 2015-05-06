package com.sofia.oppi.dbUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juanflorez on 08/04/15.
 */
public class InstalledModulesHelper extends SQLiteOpenHelper {


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
}