package com.sofia.oppi.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.Animation;

import com.sofia.oppi.animationengine.AnimationEngine;
import com.sofia.oppi.animationengine.OPPIGraphics;
import com.sofia.oppi.assets.BitmapPool;

/**
 * View for handling surface and rendering to surface in a separate thread. Used for drawing section frames.
 *
 */
public class AnimationSurface extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    // thread for running the rendering logic
    private Thread mAnimationThread = null;
    private AnimationEngine mAnimationEngine = null;
    private Bitmap mFramebuffer=null;
    // surface wrapper - control and monitor changes
    private SurfaceHolder mHolder;
    private volatile boolean running = false;
    private final String TAG = "AnimationSurface";
    private boolean mSceneRunning=false;

    public AnimationSurface( Context context, AnimationEngine engine, Bitmap framebuffer ) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback( this );
        mAnimationEngine = engine;
        mFramebuffer = framebuffer;
    }

    public void attachFrameBuffer( Bitmap buffer ){
        mFramebuffer = buffer;
    }

    @Override
    public void run() {
        Log.i( TAG, "animation draw");

        while( running ){

            if( !mHolder.getSurface().isValid() || mAnimationEngine == null || mSceneRunning == false || mFramebuffer == null )
                continue;
            // start editing the pixels in the surface
            Canvas canvas = mHolder.lockCanvas();

            synchronized ( mHolder ) {
                Rect rect=new Rect();
                // call graphics to update
                mAnimationEngine.getCurrentScene().update();
                // and draw on the framebuffer bitmap
                mAnimationEngine.getCurrentScene().present();
                // then finally draw the framebuffer with scaling
                canvas.getClipBounds( rect );
                canvas.drawBitmap( mFramebuffer, null, rect, null );
                //canvas.drawBitmap( mFramebuffer, 0, 0, null ); // no scaling
            }
            // finishes the drawing on surface
            mHolder.unlockCanvasAndPost( canvas );
        }


    }

    @Override
    public void surfaceCreated( SurfaceHolder holder ) {
        if( mAnimationEngine != null  ){
            mAnimationEngine.onMeasure( this.getMeasuredHeight(), this.getMeasuredHeight() );
        }
    }

    @Override
    public void surfaceChanged( SurfaceHolder holder, int format, int width, int height ) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surface destroyed");
        boolean retry = true;

        while( retry ){
            try{
                mAnimationThread.join();
                retry = false;
            }catch ( InterruptedException e ){
                // try again
            }
        }
    }
    public void resume() {
        Log.i( TAG, "resume");
        running = true;
        mAnimationThread = new Thread( this );
        mAnimationThread.start();
    }

    public void pause() {
        Log.i( TAG, "pause");
        running = false;
        while( true ) {
            try {
                mAnimationThread.join();
                return;
            }catch( InterruptedException e ){
                // retry
            }
        }
    }

    // TODO: can these replaced with pause/resume calls or does it disturb activity lifecycle?? TEST!
    /**
     * Method to call when scene is ready for running.
     *
     */
    public void startScene(){
        mSceneRunning=true;
    }

    /**
     * Method to call when scene must be paused/stopped.
     */
    public void stopScene(){
        mSceneRunning=false;
    }
}
