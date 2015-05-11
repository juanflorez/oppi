package com.sofia.oppi.assets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.sofia.oppi.R;
import com.sofia.oppi.animationengine.FrameImage;

import java.util.ArrayList;
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


    // TODO: background bitmaps should perhaps scaled, how about other bitmaps?
    // TODO: if we just "adapt" the given position to the real device?
    // static Bitmap createScaledBitmap( Bitmap src, int dstWidth, int dstHeight, boolean filter)
    // creates a new bitmap, scaled from an eisting bitmap when possible.


    public void loadImages( Context context, ArrayList<String> imagePaths) {
        // TODO: AsyncTask for loading all the images! (this is called by load package in own
        // thread.
        // TODO: cache bitmaps (disk cache)
        // TODO: save bitmap for later use
        Bitmap bitmap=null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        String resourceName=null;

        //==== TODO: this must be done also in the "RELEASE_CONFIG":===
        WindowManager windowManager = (WindowManager)context.getSystemService( Context.WINDOW_SERVICE );
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics( metrics );
        int realHeight = metrics.heightPixels;
        int realWidth = metrics.widthPixels;
        //==========================================


        for( int i=0; i < imagePaths.size(); i++ ){

            resourceName = imagePaths.get(i);

            options.inJustDecodeBounds = true;
            // does not allocate yet memory, just for getting the image dimensions
            BitmapFactory.decodeFile( imagePaths.get(i) , options );
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            String imageType = options.outMimeType;
            // TODO: check the original image size and check if that is ok, otherwise need to calculate new size for the image
            //int size = calculateInSampleSize( options, width, heigth );



            // finally allocate memory and load the image
            options.inJustDecodeBounds = false;
            //TODO Uncomment to optimize by avoiding scaling when possible
            bitmap = BitmapFactory.decodeFile(resourceName, options);
/*           Bitmap scaledBitmap = null;
            if( resourceName.equalsIgnoreCase( "background1") ){
                scaledBitmap = Bitmap.createScaledBitmap( bitmap, 480, 690, false );
            }
            if( scaledBitmap != null ){
                mBitmapMap.put( resourceName, scaledBitmap );
            }else{
                mBitmapMap.put( resourceName, bitmap );
            }*/
            mBitmapMap.put( resourceName, bitmap );
        }



    }

    /** releases the memory
     *
     */
    public void flush(){

     mBitmapMap.clear();

    }
}
