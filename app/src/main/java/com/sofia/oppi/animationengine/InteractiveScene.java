package com.sofia.oppi.animationengine;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by juanflorez on 15/05/15.
 */
public class InteractiveScene extends ContentScene {
    private static String TAG = "Interactive Scene";





    @Override
    public void present() {

        //TODO: REMOVE TEST
        // Just draws a background that changes colors

            // clear buffer
            Random random = new Random();
            mAnimationEngine.getGraphics().clear(random.nextInt());
            // draw first the background

            mAnimationEngine.getGraphics().drawBackground( getBitmapName() );

    }


}
