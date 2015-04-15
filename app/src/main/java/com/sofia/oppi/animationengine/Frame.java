package com.sofia.oppi.animationengine;

import java.util.ArrayList;

/**
 * This class represents the single frame in the section
 */
public class Frame{
    private String mDuration="";
    ArrayList<FrameImage> mFrameImages =null;

    public Frame( String duration, ArrayList<FrameImage> frameImages){
        mDuration=duration;
        mFrameImages = frameImages;
    }

    public Frame() {
    }

    public int getDuration(){
        // TODO: parse String in format 00:00:00 to seconds....
        return 1;
    }

    public ArrayList<FrameImage> getImages(){
        return mFrameImages;
    }

}
