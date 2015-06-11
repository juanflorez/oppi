package com.sofia.oppi.animationengine;

import android.util.Log;

import com.badlogic.androidgames.framework.Input;
import com.google.gson.annotations.SerializedName;
import com.sofia.oppi.templates.Template;
import com.sofia.oppi.templates.TemplateFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanflorez on 20/05/15.
 */
public  class ActionScene extends Scene{

    public static final String TAG = "Action_Scene";

    @SerializedName("actionImages")
    protected ArrayList<ActionImage> actionImages;

    @SerializedName("templateId")
    protected int templateId = 1;
    protected int mCurrentFrame=0;

    @SerializedName("correct_image")
    private String correctImage = "";

    @SerializedName("incorrect_image")
    private String incorrectImage = "";

    private Template template = null;

    private boolean mChanged = true;

    @Override
    public int getSceneHeight() {
        return mScreenHeight;
    }

    @Override
    public int getSceneWidth() {
        return mScreenWidth;
    }

    @Override
    public void setRoot(String path)
    {
        root = path;
        for(ActionImage image : actionImages){
            image.setRoot(path);
        }
    }

    @Override
    public void update() {}

    @Override
    public void update(List<Input.TouchEvent> events){
        template.update(events);
        update();
    }

    public void start()
    {
        setTemplate();
        if (actionImages == null) {
           Log.e(TAG, "No action images!!!");
        }
    }


    @Override
    public void present() {

        if (mAnimationEngine == null) {
            throw new RuntimeException("Engine null at present method in ActionScene");
        }

            // clear buffer
            mAnimationEngine.getGraphics().clear(0);
            // draw first the background
            mAnimationEngine.getGraphics().drawBackground(getBitmapName());
            // then draw the images of current frame
            template.draw(mAnimationEngine.getGraphics());

    }

    public  int getTemplateId() {return templateId;}

    public String getBitmapName(){
        return root+mBackground;
    }

    public String getCorrectImage() {return root+correctImage;}

    public String getIncorrectImage() {return root+incorrectImage;}

    public int setTemplate(){
        this.template = TemplateFactory.buildTemplate(this.templateId, this);
        return templateId;
    }
    @Override
    public void pause() {

    }

    @Override
    public void resume(AnimationEngine animationEngine) {

        mAnimationEngine = animationEngine;
        // TODO: perhaps we should have separate restart-method...
        mStartTime =System.nanoTime();

    }

    @Override
    public void dispose() {

    }


    public ArrayList<ActionImage> getActionImages() {
        return actionImages;
    }
}
