package com.sofia.oppi.assets;

import android.graphics.Rect;

/**
 * Interface to get view related information
 * TODO: might changes: ScreenAdapter could make basic calculations from model to real view
 */
public interface ScreenAdapter {

    /* returns the real view height */
    public int getViewHeight();
    /* return the real view width */
    public int getViewWidth();
}
