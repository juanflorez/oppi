package com.sofia.oppi.animationengine;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *
 */
public class Chapter extends ModuleElement{

    @SerializedName("Scenes")
    ArrayList<ContentScene> scenes=null;

    @SerializedName("SoundFile")
    String mAudioName="";

    public void setAudioName( String audioName ){
        mAudioName=audioName;
    }
    public String getAudioName(){
        return  root+mAudioName;
    }

    public void add( ContentScene item ) {
        if( scenes == null ){
            scenes = new ArrayList<ContentScene>();
        }
        scenes.add( item );
    }

    public void remove( ContentScene item ) {

    }
//TODO Exception for out of index
    public ContentScene getContentSceneAt(int ind) {
        ContentScene item=null;
        if( scenes != null && ind < scenes.size() ){
            item = scenes.get( ind );
        }
        return item;
    }

    public ArrayList<ContentScene> getAllScenes(){
        return scenes;
    }

   //_______ Setters and Getters for GSON to work _________


    public ArrayList<ContentScene> getScenes() {
        return scenes;
    }

    public void setScenes(ArrayList<ContentScene> scenes) {
        this.scenes = scenes;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Chapter{" +
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
}
