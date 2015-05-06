package com.sofia.oppi.animationengine;

/**
 * Created by riikka on 09/04/15.
 *
 */
public abstract class Scene extends ModuleElement {

    public Scene(){
    }

    public abstract void update();
    public abstract void present();
    public abstract void pause();
    public abstract void resume( AnimationEngine animationEngine );
    public abstract void dispose();
}
