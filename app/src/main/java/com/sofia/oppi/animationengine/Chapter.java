package com.sofia.oppi.animationengine;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 *
 */
public class Chapter implements PackageItem{
    ArrayList<PackageItem> scenes=null;

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
    public void add( PackageItem item ) {
        if( scenes == null ){
            scenes = new ArrayList<PackageItem>();
        }
        scenes.add( item );
    }

    @Override
    public void remove(PackageItem item) {

    }

    @Override
    public PackageItem getItem( int ind ) {
        PackageItem item=null;
        if( scenes != null && ind < scenes.size() ){
            item = scenes.get( ind );
        }
        return item;
    }

    public ArrayList<PackageItem> getAllItems(){
        return scenes;
    }
}
