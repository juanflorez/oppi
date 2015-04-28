package com.sofia.oppi.animationengine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.sofia.oppi.assets.BitmapPool;
import com.sofia.oppi.assets.ScreenAdapter;

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
    ScreenAdapter mScreenAdapter=null;

    public OPPIGraphics( ScreenAdapter screen, Bitmap frameBuffer, BitmapPool bitmapPool ){
        mScreenAdapter = screen;
        mFrameBuffer = frameBuffer;
        mCanvas = new Canvas( frameBuffer );
        mBitmapPool = bitmapPool;
    }

    @Override
    public void clear( int color ) {
        mCanvas.drawRGB( (color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff) );
    }

    /**
     * Draws the bitmap on the framebuffer. Adapts the bitmap position to the real screen size.
     * TODO: could converting the positions be done earlier, not here in the drawing method?
     * We need to know the lesson user has selected and get the "model/default" size and the actual screen size
     * (either from resources or after android framework has measured the size - usually just before the drawing of the view)
     *
     * @param bitmapName
     * @param x bitmap x-position
     * @param y bitmap y-position
     * @param destRect the default rectangle
     */
    @Override
    public void drawBitmap( String bitmapName, int x, int y, Rect destRect ) {
        Bitmap bitmap = mBitmapPool.getBitmap( bitmapName );
        int realScreenWidth = mScreenAdapter.getViewWidth();
        int realScreenHeight = mScreenAdapter.getViewHeight();

        if( bitmap != null && realScreenWidth != 0 && realScreenHeight != 0 ){
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            float top = 0;
            float left = 0;
            float right = 0;
            float bottom = 0;
            if( x != 0 ){

                left = realScreenWidth / ((float)destRect.width()/x);
            }
            if( y != 0 ){
                top =  realScreenHeight / ((float)destRect.height()/y);
            }
            right  = left + bitmapWidth;
            bottom = top + bitmapHeight;
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
