package com.sofia.oppi.animationengine;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.annotations.SerializedName;


/**
 * It maps to the elements in the image array in each Frame in in scene.json(s)
 */
public class FrameImage extends ModuleElement{

    final static String TAG = "FrameImage";

    @SerializedName("pos_y")
    private int mYPos=0;

    @SerializedName("pos_x")
    private int mXPos=0;

    @SerializedName("file")
    private String mBitmapName="";



    public FrameImage(String bitmapName, int positionX, int positionY){
        mYPos = positionY;
        mXPos = positionX;
        // TODO: now containes resources/ get just the resource name, LATER....HOW?
        mBitmapName = bitmapName.substring( (bitmapName.lastIndexOf( "/") + 1), bitmapName.lastIndexOf(".") );
    }






    public void setYPos( int YPos ) {
        this.mYPos = YPos;
    }




    public void setXPos( int XPos ) {
        this.mXPos = XPos;
    }



    public int getYPos() {
        return mYPos;
    }

    public int getXPos() {
        return mXPos;
    }

    public String getBitmapName() {
        Log.d("FRAME IMAGE: ",root+mBitmapName);
        return root+mBitmapName;
    }

    public void setRoot(String path){ root = path;}

    public String getRoot() {return root;}
    @Override
    public String toString() {
        return "FrameImage{" +
                 ", mYPos=" + mYPos +
                 ", mXPos=" + mXPos +
                 ", mBitmapName='" + mBitmapName + '\'' +
                '}';
    }
}
