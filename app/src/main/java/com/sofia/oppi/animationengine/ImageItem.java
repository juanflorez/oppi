package com.sofia.oppi.animationengine;

import android.graphics.Bitmap;

/**
 *
 */
public class ImageItem {
    private int mYPos=0;
    private int mXPos=0;
    private String mBitmapName="";
    private Bitmap mBitmap=null;

    public ImageItem( String bitmapName, int positionX, int positionY ){
        mYPos = positionY;
        mXPos = positionX;
        mBitmapName = bitmapName;
    }

    public void setYPos( int YPos ) {
        this.mYPos = YPos;
    }

    public void setXPos( int XPos ) {
        this.mXPos = XPos;
    }

    public void setBitmapName( String BitmapName ) {
        this.mBitmapName = BitmapName;
    }

    public void setBitmap(Bitmap Bitmap) {
        this.mBitmap = Bitmap;
    }
    public int getYPos() {
        return mYPos;
    }

    public int getXPos() {
        return mXPos;
    }

    public String getBitmapName() {
        return mBitmapName;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
