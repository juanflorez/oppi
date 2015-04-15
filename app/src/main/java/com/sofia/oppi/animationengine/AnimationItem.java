package com.sofia.oppi.animationengine;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.util.Log;


/**
 * This class represents a single animated item in the frame
 *
 *
 */
public class AnimationItem{
    String mName="";
    String mBitmapName="";
    int mPositionX = 0;
    int mPositionY = 0;
    String mAnimatedProperty;
    int mDuration=0;
    int mStartPos=0;
    int mEndPos=0;
    int mStartTime=0;
    Bitmap bitmap = null;
    int mHeight=0;
    int mWidth=0;
    int mRepeatMode;
    int mRepeatCount;
    ITEM_TYPE mType;
    boolean mClickable=false;
    int mBackgroundColor;
    String mDescription;
    String mAudioName;
    ACTION mAction;
    private String TAG = "AnimationItem";

    public enum ITEM_TYPE { SCREEN, INTERACTIVE };
    // TODO: use these?
    public enum ACTION { NONE, ROTATE, FADE, SHAKE, PULSE, DISAPPEAR, DISAPPEAR_LEFT, DISAPPEAR_RIGHT, DISAPPEAR_BOTTOM, DISAPPEAR_TOP };
    public enum ANIMATED_PROPERTY { NONE, X, Y, OPACITY, WIDTH, HEIGHT, COLOR };
    public enum ANIMATION_POSITIONS { VIEWPORT_TOP, VIEWPORT_BOTTOM, VIEWPORT_LEFT, VIEWPORT_RIGHT };


    public void setBackgroundColor( int backgroundColor ) {
        mBackgroundColor = backgroundColor;
    }

    public void setDescription( String description ) {
        mDescription = description;
    }

    // *********TODO: REMOVE??****************************************
    public boolean isClickable() {
        return mClickable;
    }

    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public int getStartTime() {
        return mStartTime;
    } // NOTE: if we need to have kind of start time ie. "2 seconds after" we can use ValueAnimator.setStartDelay( long );

    public void setStartTime(int startTime) {
        this.mStartTime = startTime;
    }
    //*****************************************************************

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap( Bitmap bitmap ) {
        this.bitmap = bitmap;
        this.mHeight = this.bitmap.getHeight();
        this.mWidth = this.bitmap.getWidth();
    }
    public int getHeight() {
        return mHeight;
    }
    public int getWidth() {
        return mWidth;
    }
    public String getName() {
        return mName;
    }

    public void setName( String name ) {
        this.mName = name;
    }
    public int getY() {
        return mPositionY;
    }
    public void setY( int positionY ) {
        Log.d( TAG, this.getName() + " setY: " + positionY);
        this.mPositionY = positionY;
    }
    public int getX() {
        //Log.d( TAG, " get X: " + mPositionX );
        return mPositionX;
    }
    public void setX( int positionX ) {
        Log.d( TAG, this.getName() + " setX: " + positionX );
        this.mPositionX = positionX;
    }
    public int getDuration() {
        return mDuration;
    }
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public int getBackgroundColor() {
        //return  0xFFD6EBFF;
        return  mBackgroundColor;
    }

    public void setBitmapName( String bitmap ) {
        mBitmapName = bitmap;
    }
    public String getBitmapName() {
        return mBitmapName;
    }
    public void setAnimationProperty( String animatedProperty ) {
        mAnimatedProperty = animatedProperty;
    }
    public String getAnimationProperty() {
        return mAnimatedProperty;
    }
    public void setStartPos( int startPosition ) {
        mStartPos = startPosition;
    }
    public int getStartPos() {
        return mStartPos;
    }
    public void setEndPos( int endPosition ) {
        mEndPos = endPosition;
    }
    public int getEndPos() {
        return mEndPos;
    }
    public void setAction( ACTION action ) {
        mAction = action;
    }
    public ACTION getAction() {
        return mAction;
    }
    public void setRepeatMode( String repeatMode ) {

        if( repeatMode.equalsIgnoreCase( "REVERSE") ){
            mRepeatMode = ValueAnimator.REVERSE;
        }else{
            mRepeatMode = ValueAnimator.RESTART;
        }
    }
    public int getRepeatMode() {
        return mRepeatMode;
    }
    public void setRepeatCount( int repeatCount ) {
        mRepeatCount = repeatCount;
    }
    public int getRepeatCount() {
        return mRepeatCount;
    }
    public ITEM_TYPE getType() {
        return mType;
    }
    public void setAudioName( String audio ) {
        mAudioName = audio;
    }
    public String getAudioName() {
        return mAudioName;
    }

}
