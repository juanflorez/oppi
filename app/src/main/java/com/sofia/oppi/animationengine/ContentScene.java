package com.sofia.oppi.animationengine;

import android.util.Log;

import com.badlogic.androidgames.framework.Input;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by riikka on 02/04/15.
 * It maps to scene descriptor in ContentChapter.json
 */
public class ContentScene extends Scene {
    private static final String TAG = "CONT_SCENE";

    protected int mSceneAudioMarkTime=0;
    protected int mCurrentFrame=0;
    protected ArrayList<Frame> mFrames=null;
    protected int mPresentedFrame=1;


    public String getJsonFile() {
        return mJsonFile;
    }

    /**The amount of milliseconds after this scene should start
     * Assumes a well formed string "HH:MM:SS" which is
     * reinforced at parse time. (TODO)
     * */

    public int getStartTime() {
        int result=-1;
        try {
            result = ModuleGsonParser.getMilliSeconds(mStartTimeString);
        } catch (Exception e) {
            Log.e(TAG, "Wrong start time "+ mStartTimeString);
            e.printStackTrace();
        }
        return result;
    }

    public String getBitmapName(){
        return root+mBackground;
    }

    public int getSceneHeight(){
        return mScreenHeight;
    }

    public int getSceneWidth(){
        return mScreenWidth;
    }

    public void setFrames( ArrayList<Frame> frames ){
        mFrames=frames;
    }

    public void addFrame(Frame frame){mFrames.add(frame);}

    public ArrayList<Frame> getFrames() {return mFrames;}

    @Override
    public void setRoot(String path)
    {
        root = path;
        for(int i = 0; i< mFrames.size(); i++){
            mFrames.get(i).setRoot(path);
        }
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
    public void update(List<Input.TouchEvent> events){
        update();
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
                    mAnimationEngine.getGraphics().drawBitmap( image.getBitmapName(), image.getXPos(), image.getYPos() );
                    Log.d(TAG, "DRAW Image: "+image.getBitmapName() + "in FRAME "+mFrames.get(mCurrentFrame).getmName());
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(
                "ContentScene{" +
                "mJsonFile='" + mJsonFile + '\'' +
                ", mBackground='" + mBackground + '\'' +
                ", mScreenHeight=" + mScreenHeight +
                ", mScreenWidth=" + mScreenWidth +
                ", mDestRect=" + mDestRect +
                ", mFrames=" + mFrames +
                ", mCurrentFrame=" + mCurrentFrame +
                ", mPresentedFrame=" + mPresentedFrame +
                ", mSceneAudioMarkTime=" + mSceneAudioMarkTime +
                ", mAnimationEngine=" + mAnimationEngine +
                ", mStartTime=" + mStartTime +
                '}'
        );
        for(int i=0; i< mFrames.size(); i++){

            builder.append('\n');
            builder.append(mFrames.get(i).toString());
        }

        return builder.toString();
    }
}
