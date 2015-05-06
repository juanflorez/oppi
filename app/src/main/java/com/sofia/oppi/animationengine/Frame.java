package com.sofia.oppi.animationengine;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This class represents the single frame in the section
 * It maps to scene.json (s)
 */
public class Frame extends ModuleElement{

    @SerializedName("duration")
    private String mDuration="";

    @SerializedName("name")
    private String mName;

    @SerializedName("images")
    ArrayList<FrameImage> mFrameImages =null;

    public Frame( String duration, ArrayList<FrameImage> frameImages){
        mDuration=duration;
        mFrameImages = frameImages;
    }

    public Frame() {
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getDuration(){
        // TODO: parse String in format 00:00:00 to seconds....
        return 1;
    }

    public ArrayList<FrameImage> getImages(){
        return mFrameImages;
    }

    @Override

    public void setRoot(String path){
        root = path;
        for(int i=0;i<mFrameImages.size();i++){
            mFrameImages.get(i).setRoot(path);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('\n');
        builder.append("Frame{" +
                "mDuration='" + mDuration + '\'' +
                ", mName='" + mName + '\'' +
                '}' +
                '\n');
        for (int i =0; i< mFrameImages.size();i++) {
            builder.append(" Image :");
            builder.append(mFrameImages.get(i).toString());
            builder.append('\n');
        }

        return builder.toString();

    }
}
