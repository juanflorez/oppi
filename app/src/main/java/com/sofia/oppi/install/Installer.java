package com.sofia.oppi.install;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.JsonReader;
import android.util.Log;

import com.sofia.oppi.Constants;
import com.sofia.oppi.animationengine.ContentPackage;
import com.sofia.oppi.animationengine.JSONPackageParser;
import com.sofia.oppi.animationengine.ModuleGsonParser;
import com.sofia.oppi.dbUtils.DbModules;
import com.sofia.oppi.dbUtils.InstalledModulesHelper;
import com.sofia.oppi.dbUtils.StringXtractor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;


/**
 * Created by juanflorez on 08/04/15.
 * Getting a list of packages from "somewhere", it downloads them to
 * the storage selected in settings, and populates the installed modules
 * It assumes the existence of a file called chapter1.json
 * TODO: A MODULE WITH MULTIPLE CHAPTERS
 * TODO: Settings to choose storage location
 * database. (Singleton)
 */
public class Installer {
   private final String TAG = "DBInstaller";
   private InstalledModulesHelper dbHelper;

// Singleton implementation
    private static volatile Installer instance;
    private Installer(Context context) {
       dbHelper = new InstalledModulesHelper(context);
    }

/* the context passes all the way through not pretty, but we assume there is only
 * one Context
 */
    public static Installer getInstance(Context context) {
        if (instance == null ) {
            synchronized (Installer.class) {
                if (instance == null) {
                    instance = new Installer(context);
                }
            }
        }

        return instance;
    }
/** Adds a new module to the database
*   If the module exists in the db, and moduleOk, do nothing
*/
//TODO: If the module exists, replace it.
   public int registerPackage (ContentPackage modulePackage, String uri ) {

       SQLiteDatabase db = dbHelper.getWritableDatabase();
       db.execSQL(DbModules.CREATE_MODULES_TABLE);
       if(!checkForInstallModule(modulePackage)) {
           ContentValues values = new ContentValues();

           values.put(DbModules.InstModule.MODULE_ID,modulePackage.getPackageID());
           values.put(DbModules.InstModule.MODULE_NAME, modulePackage.getName());
           values.put(DbModules.InstModule.MODULE_VERSION, modulePackage.getPackageVersion());
           values.put(DbModules.InstModule.ENGINE_VERSION, modulePackage.getEngineVersion());
           values.put(DbModules.InstModule.MINIMUM_RES, modulePackage.getMinResolution());
           values.put(DbModules.InstModule.MAXIMUM_RES, modulePackage.getMaxResolution());
           values.put(DbModules.InstModule.SMALL_ICON,  modulePackage.getSmallIcon());
           values.put(DbModules.InstModule.MEDIUM_ICON, modulePackage.getMediumIcon());
           values.put(DbModules.InstModule.BIG_ICON,    modulePackage.getBigIcon());
           values.put(DbModules.InstModule.DURATION, modulePackage.getDuration());
           values.put(DbModules.InstModule.MODULE_ROOT, uri);
           values.put(DbModules.InstModule.CORRECT, 1);
           // Insert the new row, returning the primary key value of the new row
           long newRowId;
           newRowId = db.insert(
                   DbModules.InstModule.TABLE_NAME,
                   null,
                   values);
           Log.i("Item added to db", modulePackage.getName() + " " + newRowId);
           return 1;
       } else {
           Log.e("FAILED to add Item " + modulePackage.getPackageID() + " to db", modulePackage.getName());
           return 0;
       }
   }

    private boolean checkForInstallModule(ContentPackage modulePackage) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
          DbModules.InstModule.MODULE_ID,
          DbModules.InstModule.CORRECT
        };

        String selectionClause = DbModules.InstModule.MODULE_ID + " = CAST (? as INTEGER) " ;
        String[] values = {modulePackage.getPackageID().toString()};
        String sortOrder = DbModules.InstModule.MODULE_ID + " DESC";

        Cursor cursor = db.query(
                DbModules.InstModule.TABLE_NAME,
                projection,
                selectionClause,
                values,
                null,
                null,
                sortOrder
        );
        if ( cursor.getCount() > 0)
        {
            Log.i(TAG,"The item exists in the DB");
            return true;
        }
        Log.i(TAG,"The item " + modulePackage.getPackageID().toString() + " exists in the DB");
        // TODO check if the module is OK
        return false;

    }


    public void deleteDatabase(){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        db.execSQL(DbModules.DELETE_MODULES_TABLE);


    }

    public void registerDirectory(String uri) {


        //TODO get the JSON package from the uri/content.json
        //pass it to the database
        //GSON: Using gson to create this content package
        ContentPackage pack;
        try {
            pack = ModuleGsonParser.getContentPackage(uri);
            registerPackage(pack, uri);
            Log.d(TAG, "PARSED: "+pack.getName() );
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Handle this
        }


    }
}

