package com.sofia.oppi.animationengine;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.sofia.oppi.assets.BitmapPool;

/**
 *
 */
public class OPPIGraphics implements Graphics {
    // for drawing on the mutable bitmap (mFrameBuffer)
    Canvas mCanvas=null;
    // Mutable bitmap, whichs is updated on the basis of current Chapter/Scene. Actual drawing of the bitmap
    // is done by AnimationSurface, which holds the reference of this bitmap
    Bitmap mFrameBuffer=null;
    BitmapPool  mBitmapPool=null;

    public OPPIGraphics( Bitmap frameBuffer, BitmapPool bitmapPool ){
        mFrameBuffer = frameBuffer;
        mCanvas = new Canvas( frameBuffer );
        mBitmapPool = bitmapPool;
    }

    @Override
    public void clear( int color ) {
        mCanvas.drawRGB( (color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff) );
    }

    @Override
    public void drawBitmap( String bitmapName, int x, int y ) {
        Bitmap bitmap = mBitmapPool.getBitmap( bitmapName );
        if( bitmap != null ){
            mCanvas.drawBitmap( bitmap, x, y, null );
        }
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }
}
