package com.sofia.oppi.assets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sofia.oppi.R;

import java.util.HashMap;


/**
 * Created by RiikkaV on 20.2.2015.
 * Class for loading images files.
 * TODO: some bitmaps (and sounds) that are common to all animations/lessons could be included to resources!
 */
public class BitmapPool {
    private static BitmapPool instance;
    Context mAppContext=null;
    HashMap<String, Bitmap> mBitmapMap=null;

    private BitmapPool() {
        mBitmapMap = new HashMap<String, Bitmap>();
    }

    public static BitmapPool getInstance(){
        if( instance == null ){
            instance = new BitmapPool();
        }
        return instance;
    }

    public Bitmap getBitmap( String name )
    {
        Bitmap bitmap=null;
        bitmap = mBitmapMap.get( name );
        return bitmap;
    }

    /*
      DEBUG_CONFIG
      THIS IS JUST FOR TESTING (to read image resources, later from server)
     */
    public void loadImages( Context context ) {
        // TODO: AsyncTask for loading all the images!
        // TODO: cache bitmaps (disk cache)
        // TODO: save bitmap for later use
        Bitmap bitmap=null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        String resourceName=null;

        int[] imageResources = { R.drawable.advance, R.drawable.back, R.drawable.background1, R.drawable.background2,
                R.drawable.background3, R.drawable.eight, R.drawable.five, R.drawable.four, R.drawable.nine,
                R.drawable.one, R.drawable.quit, R.drawable.seven, R.drawable.six, R.drawable.three, R.drawable.two,
                R.drawable.zero };

        for( int i=0; i < imageResources.length; i++ ){

            resourceName = context.getResources().getResourceEntryName( imageResources[i] );

            options.inJustDecodeBounds = true;
            // does not allocate yet memory, just for getting the image dimensions
            BitmapFactory.decodeResource( context.getResources(), imageResources[i], options );
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            String imageType = options.outMimeType;
            // TODO: check the original image size and check if that is ok, otherwise need to calculate new size for the image
            //int size = calculateInSampleSize( options, width, heigth );

            // finally allocate memory and load the image
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeResource( context.getResources(), imageResources[i], options );
            mBitmapMap.put( resourceName, bitmap );
        }
    }
}
