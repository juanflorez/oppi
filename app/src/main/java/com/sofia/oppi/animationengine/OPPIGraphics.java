package com.sofia.oppi.animationengine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.sofia.oppi.assets.BitmapPool;

/**
 *
 */
public class OPPIGraphics implements Graphics {
    private static final String TAG = "OPPIGraphics";
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
        Bitmap bitmap = mBitmapPool.getBitmap(bitmapName);

        if( bitmap != null ){
            float top = y;
            float left = x;
            float right = left + bitmap.getWidth();
            float bottom = top + bitmap.getHeight();
            RectF rect = new RectF(left,top,right,bottom);
            /*
            Uncomment for testing without position
             */
        //  RectF rect = new RectF(0,0,  mFrameBuffer.getWidth(), mFrameBuffer.getHeight());
            mCanvas.drawBitmap( bitmap, null, rect, null );
        }else{
          Log.e(TAG, "Bitmap: " + bitmapName + " Not found at drawBitmap");
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
        }else {
            Log.e(TAG, "Bitmap: " + bitmapName + "Not found");
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
