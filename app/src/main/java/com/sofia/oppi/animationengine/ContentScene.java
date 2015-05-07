package com.sofia.oppi.animationengine;

import android.graphics.Rect;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * Created by riikka on 02/04/15.
 * It maps to scene descriptor in Chapter.json
 */
public class ContentScene extends Scene {
    @SerializedName("SceneFile")
    private String mJsonFile="";

    @SerializedName("background")
    private String mBackground="";

    @SerializedName("screenHight")
    private int mScreenHeight;

    @SerializedName("screenWidth")
    private int mScreenWidth;

    private Rect mDestRect;

    private ArrayList<Frame> mFrames=null;
    private int mCurrentFrame=0;
    private int mPresentedFrame=1;
    private int mSceneAudioMarkTime=0;
    private AnimationEngine mAnimationEngine=null;
    private float mStartTime=0.0f;



    public String getJsonFile() {
        return mJsonFile;
    }

    public int getStartTime() {
        return mSceneAudioMarkTime;
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
