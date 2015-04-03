package com.sofia.oppi.animationengine;

import android.graphics.Bitmap;

/**
 * Itenrface for representing an item in the package
 * COMPOSITE PATTERN!
 *
 */
public interface PackageItem {
    int mPosX=0;
    int mPosY=0;
    int mHeight=0;
    int mWidth=0;
    String mName="DEFAULT_CONTENT_ITEM";
    String mBitmapName="";
    Bitmap mBitmap=null;

    public void setName( String name );
    public String getName();
    public void setHeight( int height );
    public int getHeight();
    public void setX( int xPos );
    public int getX();
    public void setY( int yPos );
    public int getY();
    public void setBitmapName( String bitmapName );
    public String getBitmapName();
    public void setBitmap( Bitmap bitmap );
    public Bitmap getBitmap();

    public void add( PackageItem item );
    public void remove( PackageItem item );
    public PackageItem getItem( int ind );
}
