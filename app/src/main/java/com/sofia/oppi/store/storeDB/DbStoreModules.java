package com.sofia.oppi.store.storeDB;

import android.provider.BaseColumns;


/**
 * Created by juanflorez on 08/04/15.
 * Following the suggestions from http://developer.android.com/training/basics/data-storage/databases.html
 * This class holds the schema of the database, so all the constants are in a central place.
 */
@Deprecated
public class DbStoreModules {

    public static final String DB_NAME = "oppi.db";
    public static final int DB_VERSION = 1;
    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String COMMA     = " , ";
    public static final String CREATE_TABLE;
    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " +
            StoreModules.TABLE_NAME;

    static {
        CREATE_TABLE = "" +
                "CREATE TABLE " + StoreModules.TABLE_NAME + " ( " +
                StoreModules._ID + " INTEGER PRIMARY KEY, " +
                StoreModules.PACKAGE_ID     + TEXT_TYPE + COMMA +
                StoreModules.NAME           + TEXT_TYPE + COMMA +
                StoreModules.DURATION       + TEXT_TYPE + COMMA +
                StoreModules.URL            + TEXT_TYPE + COMMA +
                StoreModules.PRICE          + TEXT_TYPE + COMMA +
                StoreModules.LANGUAGE       + TEXT_TYPE + COMMA +
                StoreModules.ICON               + TEXT_TYPE + COMMA +
                StoreModules.PACKAGE_VERSION    + TEXT_TYPE + COMMA +
                StoreModules.ENGINE_VERSION     + TEXT_TYPE + COMMA +
                StoreModules.MINIMUM_RESOLUTION + TEXT_TYPE + COMMA +
                StoreModules.MAX_RESOLUTION     + TEXT_TYPE + COMMA +
                StoreModules.SMALL_ICON         + TEXT_TYPE + COMMA +
                StoreModules.MEDIUM_ICON        + TEXT_TYPE + COMMA +
                StoreModules.BIG_ICON           + TEXT_TYPE + COMMA +
                " ) ";
    }


    /**
     * Empty constructor
     **/
    public DbStoreModules() {}

    /*
    * Matches the fields from
    * https://docs.google.com/spreadsheets/d/1Xb2GuEl8SRNI8Zi4iOv-OIde3HcbLmpi9legBXYDpNw/edit#gid=0
    *
    * */

    public static abstract class StoreModules implements BaseColumns {

        public static final String TABLE_NAME     = "storeModules";
        public static final String PACKAGE_ID     = "PackageID";
        public static final String NAME           = "Name";
        public static final String DURATION       = "Duration";
        public static final String URL            = "url";
        public static final String PRICE          = "price";
        public static final String LANGUAGE       = "language";
        public static final String ICON                = "icon";
        public static final String PACKAGE_VERSION     = "packageVersion";
        public static final String ENGINE_VERSION      = "engineVersion";
        public static final String MINIMUM_RESOLUTION          = "MinimumResolution";
        public static final String MAX_RESOLUTION       = "MaximumResolution";
        public static final String SMALL_ICON        = "SmallIcon";
        public static final String MEDIUM_ICON       = "MediumIcon";
        public static final String BIG_ICON        = "BigIcon";



    }

}

