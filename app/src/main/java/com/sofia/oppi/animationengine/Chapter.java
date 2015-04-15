package com.sofia.oppi.animationengine;

import java.util.ArrayList;

/**
 *
 */
public class Chapter{
    ArrayList<ContentScene> scenes=null;
    String mAudioName="";

    public void setAudioName( String audioName ){
        mAudioName=audioName;
    }
    public String getAudioName(){
        return  mAudioName;
    }

    public void add( ContentScene item ) {
        if( scenes == null ){
            scenes = new ArrayList<ContentScene>();
        }
        scenes.add( item );
    }

    public void remove( ContentScene item ) {

    }

    public ContentScene getItem( int ind ) {
        ContentScene item=null;
        if( scenes != null && ind < scenes.size() ){
            item = scenes.get( ind );
        }
        return item;
    }

    public ArrayList<ContentScene> getAllItems(){
        return scenes;
    }
}
