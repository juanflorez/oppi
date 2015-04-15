package com.sofia.oppi.animationengine;

import java.util.ArrayList;

/**
 * Created by riikka on 02/04/15.
 *
 */
public class ContentScene extends Scene {
    private String mJsonFile="";
    private String mBackground="";
    private int mScreenHeight;
    private int mScreenWidth;
    private ArrayList<Frame> mFrames=null;
    private int mCurrentFrame=0;
    private int mPresentedFrame=1;
    private int mSceneAudioMarkTime=0;
    private AnimationEngine mAnimationEngine=null;
    private float mStartTime=0.0f;

    public ContentScene(String jsonFile, String startTime, String background, String height, String width ) {
        super();
        mJsonFile = jsonFile;
        // TODO: PARSE THIS FOR EXAMPLE "00:00:14" NOW TAKES ONLY SECONDS...USE REGEx?
        String seconds = startTime.substring( (startTime.lastIndexOf( ":") + 1));
        mSceneAudioMarkTime = Integer.parseInt( seconds );
        // TODO; now contains path /resources/nama.png, get JUST the name. CHANGE THIS....LATER
        mBackground = background.substring( (background.lastIndexOf( "/") + 1), background.lastIndexOf(".") );
        mScreenHeight = Integer.parseInt( height );
        mScreenWidth = Integer.parseInt( width );
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
            mAnimationEngine.getGraphics().drawBitmap( getBitmapName(), 0, 0 );
            // then draw the images of current frame
            if( images != null ){
                for( int i=0; i < images.size(); i++ ){
                    FrameImage image = images.get( i );
                    mAnimationEngine.getGraphics().drawBitmap( image.getBitmapName(), image.getXPos(), image.getYPos() );
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
