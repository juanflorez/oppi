package com.sofia.oppi.animationengine;

import android.graphics.Rect;

/**
 * Created by riikka on 09/04/15.
 *
 */
public interface Graphics {

    public void clear( int color );
    public void drawBitmap( String bitmapName, int x, int y );
    public void drawBackground( String bitmapName );
    public void drawBitmap( String bitmapName, Rect rect);
    public int getHeight();
    public int getWidth();

}
