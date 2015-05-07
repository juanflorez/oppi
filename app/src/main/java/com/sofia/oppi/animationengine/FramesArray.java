package com.sofia.oppi.animationengine;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by juanflorez on 04/05/15.
 */
public class FramesArray {
    @SerializedName("framesArray")
    ArrayList<Frame> frames;

    public FramesArray() {
    }

    public ArrayList<Frame> getFrames() {
        return frames;
    }

    public void setFrames(ArrayList<Frame> frames) {
        this.frames = frames;
    }
}
