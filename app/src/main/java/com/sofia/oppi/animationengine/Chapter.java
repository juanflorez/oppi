package com.sofia.oppi.animationengine;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by juanflorez on 18/05/15.
 */
public abstract class Chapter extends ModuleElement{

    public enum Status {
        NOT_READY,
        READY,
        PLAYING_CONTENT,
        SHOWING_ACTION
    }
    protected Status status = Status.NOT_READY;
    public abstract void setAudioName(String audioName);

    public abstract String getAudioName();

    public abstract void add( ContentScene item);

    //public abstract void remove(ContentScene item);

    //TODO Exception for out of index
        public abstract Scene getSceneAt(int ind);

    public abstract ArrayList<ContentScene> getAllContentScenes();

    public abstract ArrayList<ContentScene> getContentScenes();

    public abstract void setContentScenes(ArrayList<ContentScene> scenes);

    //public abstract Scene getNextScene();

    @Override
    public abstract String toString();

    @Override
    public abstract void setRoot(String path);
}
