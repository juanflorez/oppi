package com.sofia.oppi.animationengine;

import android.graphics.Rect;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * Created by riikka on 02/04/15.
 *
 */
public class ContentScene extends Scene {
    private String mJsonFile="";
    private String mBackground="";
    private int mScreenHeight;
    private int mScreenWidth;
    private Rect mDestRect;
    private ArrayList<Frame> mFrames=null;
    private int mCurrentFrame=0;
    private int mPresentedFrame=1;
    private int mSceneAudioMarkTime=0;
    private AnimationEngine mAnimationEngine=null;
    private float mStartTime=0.0f;

    public ContentScene(String jsonFile, String startTime, String background, String height, String width ) {
        super();
        mJsonFile = jsonFile;
        String[] times = startTime.split( ":", 3);
        mSceneAudioMarkTime= parseInt(times[2]) + parseInt(times[1])*60 + parseInt(times[0])*3600;

        // TODO; now contains path /resources/nama.png, get JUST the name. CHANGE THIS....LATER
        mBackground = background.substring( (background.lastIndexOf( "/") + 1), background.lastIndexOf(".") );
        mScreenHeight = Integer.parseInt( height );
        mScreenWidth = Integer.parseInt( width );
        mDestRect = new Rect( 0, 0, mScreenWidth, mScreenHeight );
    }

    public String getJsonFile() {
        return mJsonFile;
    }

    public int getStartTime() {
        return mSceneAudioMarkTime;
    }

    public String getBitmapName(){
        return mBackground;
    }

    public int getSceneHeight(){
        return mScreenHeight;
    }

    public int getSceneWidth(){
        return mScreenWidth;
    }

    public void add( ArrayList<Frame> frames ){
        mFrames=frames;
    }

    @Override
    public void update() {
        // calculate delta time
        float deltaTime=(System.nanoTime()-mStartTime) / 1000000000.0f;
        Frame currentFrame = mFrames.get( mCurrentFrame );
        int frameDuration = currentFrame.getDuration();

        if( frameDuration  <= deltaTime ){
            mCurrentFrame = (mCurrentFrame+1) < mFrames.size() ? (mCurrentFrame+1) : 0;
            mStartTime =System.nanoTime();
        }

    }

    @Override
    public void present() {
        // draw only if frame has changed
        if( mPresentedFrame != mCurrentFrame ){
            ArrayList<FrameImage> images = mFrames.get( mCurrentFrame ).getImages();
            // clear buffer
            mAnimationEngine.getGraphics().clear( 0 );
            // draw first the background

            mAnimationEngine.getGraphics().drawBackground( getBitmapName() );
            // then draw the images of current frame
            if( images != null ){
                for( int i=0; i < images.size(); i++ ){
                    FrameImage image = images.get( i );
                    mAnimationEngine.getGraphics().drawBitmap( image.getBitmapName(), image.getXPos(), image.getYPos(), mDestRect );
                }
            }
            mPresentedFrame=mCurrentFrame;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume( AnimationEngine animationEngine ) {
        mAnimationEngine = animationEngine;
        // TODO: perhaps we should have separate restart-method...
        mStartTime =System.nanoTime();
        mCurrentFrame=0;
        mPresentedFrame=1;
    }

    @Override
    public void dispose() {

    }
}
