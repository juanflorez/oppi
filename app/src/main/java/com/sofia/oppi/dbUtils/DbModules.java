package com.sofia.oppi.dbUtils;

import android.provider.BaseColumns;

/**
 * Created by juanflorez on 08/04/15.
 * Following the suggestions from http://developer.android.com/training/basics/data-storage/databases.html
 * This class holds the schema of the database, so all the constants are in a central place.
 */
public class DbModules {

    public static final String DB_NAME = "oppi.db";
    public static final int DB_VERSION = 1;
    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String COMMA     = " , ";
    public static final String CREATE_TABLE;
    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " +
                                                 InstModule.TABLE_NAME;

    static {
        CREATE_TABLE = "" +
                "CREATE TABLE " + InstModule.TABLE_NAME + " ( " +
                InstModule._ID + " INTEGER PRIMARY KEY, " +
                InstModule.MODULE_ROOT + TEXT_TYPE + COMMA +
                InstModule.MODULE_ID + TEXT_TYPE + COMMA +
                InstModule.MODULE_NAME + TEXT_TYPE + COMMA +
                InstModule.MODULE_VERSION + TEXT_TYPE + COMMA +
                InstModule.ENGINE_VERSION + TEXT_TYPE + COMMA +
                InstModule.MINIMUM_RES + TEXT_TYPE + COMMA +
                InstModule.MAXIMUM_RES + TEXT_TYPE + COMMA +
                InstModule.SMALL_ICON + TEXT_TYPE + COMMA +
                InstModule.MEDIUM_ICON + TEXT_TYPE + COMMA +
                InstModule.BIG_ICON + TEXT_TYPE + COMMA +
                InstModule.DURATION + TEXT_TYPE + COMMA +
                InstModule.CORRECT + INTEGER_TYPE +
                " ) ";
    }


    /**
     * Empty constructor
    **/
    public DbModules() {}

    /*Inner classes for each table*/

    public static abstract class InstModule implements BaseColumns {

        public static final String TABLE_NAME    = "modules";
        public static final String MODULE_ROOT   = "moduleRoot"; // This points to the module path
        public static final String MODULE_ID     = "moduleId";      //Package id
        public static final String MODULE_NAME    = "moduleName";
        public static final String MODULE_VERSION = "moduleVersion"; //Package version
        public static final String ENGINE_VERSION = "moduleEngine";
        public static final String MINIMUM_RES    = "moduleLowestRes";
        public static final String MAXIMUM_RES    = "moduleMaximumRes";
        public static final String SMALL_ICON     = "moduleSmIcon";
        public static final String MEDIUM_ICON    = "moduleMedIcon";
        public static final String BIG_ICON       = "moduleBigIcon";
        public static final String DURATION       = "moduleDuration";
        public static final String CORRECT        = "moduleOK"; //if false, do not load, try to install again
    }
}

