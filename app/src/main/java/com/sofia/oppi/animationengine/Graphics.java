package com.sofia.oppi.animationengine;

import android.graphics.Bitmap;

/**
 * Created by riikka on 09/04/15.
 *
 */
public interface Graphics {

    public void clear( int color );
    public void drawBitmap( String bitmapName, int x, int y );
    public int getHeight();
    public int getWidth();

}
