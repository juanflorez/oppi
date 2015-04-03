package com.sofia.oppi.animationengine;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * This class represents the single frame in the section
 */
public class Frame implements PackageItem {
    private String mDuration="";
    ArrayList<ImageItem> mImageItems=null;

    public Frame( String duration, ArrayList<ImageItem> imageItems ){
        mDuration=duration;
        mImageItems=imageItems;
    }

    public Frame() {
    }

    @Override
    public void setName(String name) {

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

    @Override
    public PackageItem getItem(int ind) {
        return null;
    }
}
