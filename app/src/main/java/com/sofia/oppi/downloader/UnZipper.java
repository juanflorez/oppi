package com.sofia.oppi.downloader;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by juanflorez on 13/04/15.
 */
public class UnZipper {

    public static final String TAG = "UnZipper ";

    public static String unpackZip(String path, String zipFile, String destination)
    {
        Log.d(TAG, " Path " + path);
        Log.d(TAG, " zipFile "+ zipFile);
        Log.d(TAG, " destination "+ destination);
        InputStream is;
        ZipInputStream zis;
        String rootDir = new String();
        try
        {
            String filename;
            is = new FileInputStream(zipFile);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;


            while ((ze = zis.getNextEntry()) != null)
            {

                filename = ze.getName();

                //create sub directories
                if (ze.isDirectory()) {
                    File fmd = new File(destination + filename);
                    Log.d(TAG,destination + filename);
                    fmd.mkdirs();
                    // if it is the first, it assumes it is the root directory
                    if(rootDir.equals("")){
                        rootDir = destination+filename;
                    }
                    Log.d(TAG, "Root directory "+ rootDir);
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(destination  + filename);

                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return rootDir;
        }

        return rootDir;
    }
}
