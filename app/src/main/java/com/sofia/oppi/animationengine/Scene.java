package com.sofia.oppi.animationengine;

import android.graphics.Rect;

import com.badlogic.androidgames.framework.Input;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by riikka on 09/04/15.
 *
 */
public abstract class Scene extends ModuleElement {

    @SerializedName("SceneFile")
    protected String mJsonFile="";
    @SerializedName("background")
    protected String mBackground="";
    @SerializedName("screenHight")
    protected int mScreenHeight;
    @SerializedName("screenWidth")
    protected int mScreenWidth;
    @SerializedName("StartTime")
    protected String mStartTimeString;
    protected Rect mDestRect;
    protected AnimationEngine mAnimationEngine=null;
    protected float mStartTime=0.0f;

    public Scene(){
    }

    public abstract void update();
    public abstract void update(List<Input.TouchEvent> events);
    public abstract void present();
    public abstract void pause();
    public abstract void resume( AnimationEngine animationEngine );
    public abstract void dispose();

    public abstract int getSceneHeight();
    public abstract int getSceneWidth();


    public String getJsonFile() {
        return mJsonFile;
    }
}
