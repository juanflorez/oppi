package com.sofia.oppi.animationengine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.sofia.oppi.assets.BitmapPool;

/**
 *
 */
public class OPPIGraphics implements Graphics {
    // for drawing on the mutable bitmap (mFrameBuffer)
    Canvas mCanvas=null;
    // Mutable bitmap, which is updated on the basis of current Chapter/Scene. Actual drawing of the bitmap
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

    /**
     * Draws the bitmap on the framebuffer. The framebuffer is scaled to real view size when drawing on canvas (onDraw).
     *
     * @param bitmapName
     * @param x bitmap x-position
     * @param y bitmap y-position
     */
    @Override
    public void drawBitmap( String bitmapName, int x, int y ) {
        Bitmap bitmap = mBitmapPool.getBitmap( bitmapName );

        if( bitmap != null ){
            float top = y;
            float left = x;
            float right = left + bitmap.getWidth();
            float bottom = top + bitmap.getHeight();
            RectF rect = new RectF(left,top,right,bottom);
            mCanvas.drawBitmap( bitmap, null, rect, null );
        }
    }

    /**
     * Draws the background - always according to the framebuffer size
     * @param bitmapName
     */
    @Override
    public void drawBackground( String bitmapName ) {
        Bitmap bitmap = mBitmapPool.getBitmap( bitmapName );
        if( bitmap != null ){
            Rect frameRect = new Rect( 0,0, mFrameBuffer.getWidth(), mFrameBuffer.getHeight() );
            mCanvas.drawBitmap( bitmap,  null, frameRect,null );
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
