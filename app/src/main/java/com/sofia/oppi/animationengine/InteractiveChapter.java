package com.sofia.oppi.animationengine;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *
 */
@Deprecated
public class InteractiveChapter extends Chapter{

    private static final String TAG = "Interactive Chapter" ;
    @SerializedName("Scenes")
    ArrayList<InteractiveScene> scenes=null;

    @SerializedName("SoundFile")
    String mAudioName="";

    public void setAudioName( String audioName ){
        mAudioName=audioName;
    }
    public String getAudioName(){
        return  root+mAudioName;
    }

    @Override
    public void add(ContentScene item) {
        Log.d(TAG, "bad boy, ugly code: add ContenScene" );
    }

    @Override
    public Scene getSceneAt(int ind) {
        return null;
    }

    public void add( InteractiveScene item ) {
        if( scenes == null ){
            scenes = new ArrayList<InteractiveScene>();
        }
        scenes.add( item );
    }

    public void remove( InteractiveScene item ) {

    }
//TODO Exception for out of index
    public ContentScene getContentSceneAt(int ind) {
        ContentScene item=null;
        if( scenes != null && ind < scenes.size() ){
            item = scenes.get( ind );
        }
        return item;
    }

    public ArrayList<ContentScene> getAllContentScenes(){
        return null;
    }

   //_______ Setters and Getters for GSON to work _________


    public ArrayList<ContentScene> getContentScenes() {
        return null;
    }

    public void setContentScenes(ArrayList<ContentScene> scenes) {
        Log.d(TAG, "bad boy, ugly code" );
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ContentChapter{" +
                ", mAudioName='" + mAudioName + '\'' +
                '}'+'\n'
        );
        for (int i=0; i< scenes.size();i++){
            builder.append(
                    "SCENE: " + i + '\n' +
                            scenes.get(i).toString() +
                            '\n'
            );

        }

        return builder.toString();
    }
    @Override
    public void setRoot(String path)
    {
        root = path;
        for(int i=0; i<scenes.size();i++){
            scenes.get(i).setRoot(path);
        }
    }

    public ArrayList<InteractiveScene> getInteractiveScenes() {
        return scenes;
    }
}
