package com.sofia.oppi.animationengine;

/**
 *
 *
 */
public interface AnimationEngine {
    public Scene getCurrentScene();
    public Graphics getGraphics();
    public void onMeasure( int height, int width );
}
