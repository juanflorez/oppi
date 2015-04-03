package com.sofia.oppi.animationengine;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by riikka on 02/04/15.
 */
public class Scene implements PackageItem {
    private String mJsonFile="";
    private String mStartTime="";
    private String mBackground="";
    private String mScreenHeight="";
    private String mScreenWidth="";
    private ArrayList<PackageItem> mFrames=null;

    public Scene( String jsonFile, String startTime, String background, String height, String width) {
        mJsonFile = jsonFile;
        mStartTime =startTime;
        mBackground = background;
        mScreenHeight = height;
        mScreenWidth = width;
    }

    public String getJsonFile() {
        return mJsonFile;
    }

    public String getStartTime() {
        return mStartTime;
    }

    @Override
    public void setName( String name ) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setHeight(int height) {

    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void setX(int xPos) {

    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public void setY(int yPos) {

    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void setBitmapName(String bitmapName) {

    }

    @Override
    public String getBitmapName() {
        return null;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public void add(PackageItem item) {

    }

    @Override
    public void remove(PackageItem item) {

    }

    public void add( ArrayList<PackageItem> frames ){
        mFrames=frames;
    }

    @Override
    public PackageItem getItem(int ind) {
        return null;
    }
}
