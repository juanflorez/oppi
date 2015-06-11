package com.sofia.oppi.templates;

import android.app.Application;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.impl.AndroidInput;
import com.sofia.oppi.animationengine.ActionImage;
import com.sofia.oppi.animationengine.ActionScene;
import com.sofia.oppi.animationengine.Graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanflorez on 25/05/15.
 * Creates a grid board with the @PojoActionImage.
 */
public class TemplateOne implements Template {

    private static final String TAG ="TemplateOne" ;

    protected ActionScene mScene;
    protected ArrayList<ActionImage> mActionImages;

    private Rect[][] matrixRec;
    private ActionImage[][] matrixItems;



    public TemplateOne(int rows, int columns, ActionScene scene){

        matrixRec = new Rect[rows][columns];
        matrixItems = new ActionImage[rows][columns];
        this.mScene = scene;
        this.mActionImages = mScene.getActionImages();
        int rectWidth = mScene.getSceneWidth() / columns;
        int rectHeight = mScene.getSceneHeight() / rows;
        int top_x = 0;
        int top_y = 0;
        int bottom_x = rectWidth;
        int bottom_y = rectHeight;
        int imageIndex = 0;
        int maxIndex = mActionImages.size()-1;

        for (int row=0; row < matrixRec.length; row++ ) {
            Log.d(TAG, "Building Template row "+ row);

            for (int col = 0; col < matrixRec[row].length; col++) {
                Log.d(TAG, "Building Template col "+ row + " " + col);
                matrixRec[row][col] = new Rect(top_x, top_y , bottom_x, bottom_y);
                matrixItems[row][col] = new ActionImage(mActionImages.get(imageIndex));
                top_x = top_x + rectWidth;
                bottom_x = bottom_x + rectWidth;
                if (imageIndex < maxIndex){
                    imageIndex++;
                }else {
                    imageIndex = 0;
                }

            }
            top_x = 0;
            top_y = top_y + rectHeight;
            bottom_x = rectWidth;
            bottom_y = bottom_y + rectHeight;

        }

    }

    @Override
    public void draw(Graphics graphics) {


        for (int row=0; row < matrixRec.length; row++ ) {

            for (int col = 0; col < matrixRec[row].length; col++) {

                Log.d(TAG, "adding image " +  matrixItems[row][col].mName);
                graphics.drawBitmap( getImage(matrixItems[row][col]), matrixRec[row][col]);
            }

        }

    }

   public void update(){   }

   public void update(List<Input.TouchEvent> touchEvents) {
        if(touchEvents==null){
            touchEvents = new ArrayList<Input.TouchEvent>();
        }
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                Log.d(TAG,"Point Template: "+ (event.x) + " , "+
                                                 event.y);
                Log.d(TAG, "Center x: "+matrixRec[1][1].centerX());
                for (int row=0; row < matrixRec.length; row++ ) {

                    for (int col = 0; col < matrixRec[row].length; col++) {

                        if (matrixRec[row][col].contains(event.x, event.y)) {
                            Log.d(TAG, "Item "+ matrixItems[row][row].mName + " Activated!");
                            matrixItems[row][col].activate();
                        }
                    }

                }
            }

        }
    }


    public String getImage(ActionImage image) {

        if (image.isActivated()) {
            if(image.mCorrect){
                return mScene.getCorrectImage();
            }else {
                return mScene.getIncorrectImage();
            }
        }else {
            return image.getStartImage();
        }
    }

}
