package com.sofia.oppi.downloader;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.sofia.oppi.Constants;
import com.sofia.oppi.dbUtils.DbModules;
import com.sofia.oppi.dbUtils.DownloadingModulesHelper;
import com.sofia.oppi.install.Installer;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by juanflorez on 14/04/15.
 * This is the Broadcast Receiver to register for the "download complete event"
 * If the download is a module, it will process it.
 */
public class BrReceiver extends BroadcastReceiver{

    private static String TAG= "BrReceiver";

    List<Long> pendingModules;
    DownloadManager dm;
    Context mContext;
    DownloadingModulesHelper dbHelper;

    public  BrReceiver () {
        super();
        mContext = AppController.getInstance().getContext();
        dm = (DownloadManager) mContext.getSystemService(Activity.DOWNLOAD_SERVICE);
        dbHelper = new DownloadingModulesHelper(mContext);

    }
    @Override
    public synchronized void onReceive(Context context, Intent intent) {
             //mContext = context;
            Log.v(TAG, "Triggered On Receive" );
            DownloadManager dm = (DownloadManager) context.getSystemService(Activity.DOWNLOAD_SERVICE);

            String action = intent.getAction();


            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {

                // from the database
                Cursor cursor = queryModuleDownloads();
                int coulumnIndex = cursor.getColumnIndex(DbModules.OnGoingDownloads.QUEUE_ID);
                cursor.moveToFirst();

                // from Download Manager
                DownloadManager.Query query = new DownloadManager.Query();

                while (!cursor.isAfterLast()) {
                    Long downloadID= cursor.getLong(coulumnIndex);
                    query.setFilterById(downloadID);
                    Cursor c = dm.query(query);

                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {
                            String downloadPath = c.getString(c
                                    .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            String zipFileName = c.getString(c
                                    .getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                            Log.v("THE DOWNLOAD PATH ", Uri.parse(downloadPath).toString());
                            Log.v("THE ENV LOCATION ", Environment.
                                    getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
                            Log.v("THE ZIP FILE NAME ", zipFileName);

                            // TODO add the unZiped path to the modules database
                            String uZipedLocal =
                            unZipFile(Uri.parse(downloadPath).toString(), zipFileName);

                            if(!uZipedLocal.equals("")) {
                                install(uZipedLocal);
                                AppController.getInstance().removeFromPendingModules(downloadID);
                            }

                        }
                    }
                    c.close();
                    removeDownloadingRecord(downloadID);
                    cursor.moveToNext();

                }
                cursor.close();
            }
            closeDb();
        }


//Calls the installer to register the module in the database.

    private void install(String uZipedLocal) {
        Installer.getInstance(mContext).registerDirectory(uZipedLocal);

    }

    private Cursor queryModuleDownloads() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DbModules.OnGoingDownloads.QUEUE_ID,
                DbModules.OnGoingDownloads.LINK
        };

        Cursor cursor = db.rawQuery(" SELECT * FROM "+ DbModules.OnGoingDownloads.TABLE_NAME,null);
        return cursor;
    }

    private boolean removeDownloadingRecord(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String deleteQuery = "DELETE FROM " + DbModules.OnGoingDownloads.TABLE_NAME +
                " WHERE " + DbModules.OnGoingDownloads.QUEUE_ID + " = " + id;

        db.execSQL( deleteQuery );

        Log.v(TAG, deleteQuery);
        return true;
    }

    /**
     *  give a path and a zip file, and it iwill returned the unziped file
     *  in the patn
     * @param path
     * @param zipFile
     * @return
     */
    public String unZipFile(String path, String zipFile){
        String destination = Environment.getExternalStorageDirectory().getAbsolutePath()+
                Constants.HOME_DIR;
        // TODO Delete zip file after successful extraction
        return UnZipper.unpackZip(path, zipFile, destination);

    }

    private void closeDb(){
        dbHelper.close();

    }


}


