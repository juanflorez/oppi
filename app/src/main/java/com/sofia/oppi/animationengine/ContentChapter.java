package com.sofia.oppi.animationengine;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.sofia.oppi.assets.ContentAudioPlayer;

import java.util.ArrayList;

/**
 *
 */
public class ContentChapter extends Chapter{

    private static final String TAG = "Cont_Chapter";
    private int mCurrentSceneInd = -1;
    //TODO add gson versioning and change scens to ContentScenes ( to make it more logical)
    @SerializedName("Scenes")
    ArrayList<ContentScene> contentScenes =new ArrayList<ContentScene>();
    @SerializedName("SoundFile")
    String mAudioName="";
    @SerializedName("ActionScenes")
    ArrayList<ActionScene> actionScenes = new ArrayList<ActionScene>();
    private Scene mCurrentScene;

    private int counter = 0;


    @Override
    public void setAudioName( String audioName ){
        mAudioName=audioName;
    }
    @Override
    public String getAudioName(){
        return  root+mAudioName;
    }

    public void add( ContentScene item ) {
        if( contentScenes == null ){
            contentScenes = new ArrayList<ContentScene>();
        }
        contentScenes.add(item);
    }


    public Status prepareChapter(AnimationEngine animationEngine) {
        switch (this.status) {
            case NOT_READY:
                if (contentScenes == null) {

                    contentScenes = new ArrayList<ContentScene>();

                }
                if (actionScenes == null) {

                    actionScenes = new ArrayList<ActionScene>();

                }

                for (int i = 0; i < actionScenes.size(); i++) {

                    actionScenes.get(i).resume(animationEngine);
                }

                for (int j = 0; j < contentScenes.size(); j++) {

                    contentScenes.get(j).resume(animationEngine);
                }

                this.status = Status.READY;
                break;

            case READY:
                break;
        }
        return this.status;
    }

//TODO Exception for out of index by now, if ind > contentSences.size() + actionScenes.size()
// return last actionScene
//TODO I am NOT proud of this mess...
    /**
     * Returns the Scene at the index (action or content)
     * @param ind
     * @return the content scene, actionsene, or null if there are no more content scenes and no action
     * scenes.
     */
    @Override
    public Scene getSceneAt(int ind) {

        Scene item=null;
        int contentArraySize = contentScenes.size();
        int actionArraySize = actionScenes.size();
        // Return the last Action Scene
        if(ind > (contentArraySize+actionArraySize)&& actionArraySize>0){

            return actionScenes.get(actionArraySize-1);
        }

        // return the contentScene
        if(   ind < contentArraySize && contentArraySize > 0 ){

            return contentScenes.get( ind );
        }

        // go for Action Scene if there is one
        int newIndex = ind-contentArraySize;

        if( newIndex < actionArraySize && actionArraySize > 0){

            return actionScenes.get(newIndex);
        }
        return null; // Nothing else

    }



    public ArrayList<ContentScene> getAllContentScenes(){
        return contentScenes;
    }

// ----- play back chapter keeping the status and calculating the current scene.


    public int startPlayback(Context context, int sceneInd, AnimationEngine engine ) {


        if (this.status == Status.NOT_READY) {

            this.prepareChapter(engine);
        }

        if (getAudioName() != null) {

            this.status = Status.PLAYING_CONTENT;
            ContentAudioPlayer.getInstance().playAudio(getAudioName(), context);
            if (getSceneAt(sceneInd) instanceof ContentScene) {
                ContentAudioPlayer.getInstance().seekToPosition(((ContentScene) getSceneAt(sceneInd)).getStartTime());
            }

        } else { // no audio, only Action

            this.status = Status.SHOWING_ACTION;
        }

        mCurrentSceneInd = 0;
        mCurrentScene = getSceneAt(mCurrentSceneInd);
        return 0;
    }


    public Scene getCurrentScene() {
        Scene nextScene = getSceneAt(mCurrentSceneInd+1);
        Log.d(TAG, "getCurrentScene Status "+ this.status);
        if (counter > 0){
            counter--;
            return getSceneAt(mCurrentSceneInd);

        }
        counter =15;
        switch (this.status){

            case PLAYING_CONTENT:

                //current playback time Milliseconds
                int elapsedMilliSeconds = ContentAudioPlayer.getInstance().getCurrentPosition();

                // if the next scene is still content
                if (nextScene instanceof  ContentScene)
                {
                    int startTime = ((ContentScene)nextScene).getStartTime();
                    Log.d(TAG, "NextScene Start " + startTime);
                    if ( startTime <= elapsedMilliSeconds)
                    {
                        return getNextScene();
                    }
                    return getSceneAt(mCurrentSceneInd);
                }
                else if(nextScene==null)// Next comes Action Scene
                {
                    // will return null; --> End of the chapter
                    return getNextScene();
                }

                return getNextScene(); // It was no content scene and not null


            case SHOWING_ACTION:
                //TODO Here, get touch inputs and prepare scene, by now it does not
                //change scene
                return getSceneAt(mCurrentSceneInd);
        }

        return null; //make it crash if it reaches this point.
    }

    public int stopPlayback() {
        ContentAudioPlayer.getInstance().release();
        return 0;
    }


    private Scene getNextScene(){

        switch (this.status){

            case PLAYING_CONTENT:

                if(mCurrentSceneInd == contentScenes.size()-1){
                    if(actionScenes.size()>0){
                        if(ContentAudioPlayer.getInstance().isPlaying()){
                            return mCurrentScene;
                        }
                        this.status = Status.SHOWING_ACTION;
                        mCurrentSceneInd++;
                        mCurrentScene = getActionSceneAt(0);
                        Log.d(TAG, "Next scene: Action " + mCurrentScene.getJsonFile());
                        break;
                    }

                }
                mCurrentSceneInd++;
                mCurrentScene = getSceneAt(mCurrentSceneInd);
               break;


            case SHOWING_ACTION:

                mCurrentScene = getActionSceneAt(0);
                Log.d(TAG, "Next scene " + mCurrentScene.getJsonFile());
                break; // TODO ONLY ONE ACTION SCENE!!!

        }
        return mCurrentScene;
    }

    /**
     *
     * @return the combined number of scenes in this chapter
     */
    public int getTotalScenes(){
        return contentScenes.size()+actionScenes.size();
    }

    //_______ Setters and Getters for GSON to work _________



    public ArrayList<ContentScene> getContentScenes() {
        return contentScenes;
    }


    public void setContentScenes(ArrayList<ContentScene> scenes) {
        this.contentScenes = scenes;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ContentChapter{" +
                ", mAudioName='" + mAudioName + '\'' +
                '}'+'\n'
        );
        for (int i=0; i< contentScenes.size();i++){
            builder.append(
                    "SCENE: " + i + '\n' +
                            contentScenes.get(i).toString() +
                            '\n'
            );

        }

        return builder.toString();
    }
    @Override
    public void setRoot(String path)
    {
        root = path;
        for(int i=0; i< contentScenes.size();i++){
            contentScenes.get(i).setRoot(path);
        }

        for(int j=0; j< actionScenes.size();j++){
            actionScenes.get(j).setRoot(path);
        }
    }

    public ArrayList<ActionScene> getActionScenes() {
        if (actionScenes == null){
            return new ArrayList<ActionScene>();
        }
        return actionScenes;
    }

    public Scene getActionSceneAt(int k) {
        Scene item = null;
        if (actionScenes !=null){
            if(k< actionScenes.size()){
                return actionScenes.get(k);
            }
        }
        return item;
    }

    public void setActionScenes(ArrayList<ActionScene> actionScenes) {
        this.actionScenes = actionScenes;
    }

    public int getCurrentSceneInd() {
        return mCurrentSceneInd;
    }
}
