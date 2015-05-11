package com.sofia.oppi.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.sofia.oppi.R;
import com.sofia.oppi.animationengine.AnimationEngine;
import com.sofia.oppi.animationengine.Chapter;
import com.sofia.oppi.animationengine.ContentPackage;
import com.sofia.oppi.animationengine.ContentScene;
import com.sofia.oppi.animationengine.Graphics;
import com.sofia.oppi.animationengine.OPPIGraphics;
import com.sofia.oppi.animationengine.Scene;
import com.sofia.oppi.animationengine.SceneObserver;
import com.sofia.oppi.assets.BitmapPool;
import com.sofia.oppi.assets.ContentAudioPlayer;
import com.sofia.oppi.assets.PackagePool;

import java.util.ArrayList;


/**
 *
 */
public class ContentActivity extends Activity implements AnimationEngine, SceneObserver{
    private static final String TAG = "CONTENT_ACTIVITY";
    private AnimationSurface mAnimationSurface=null;
    private Long mPackageID;
    private String mAudioFile="";
    private Scene mCurrentScene=null;
    private int mCurrentSceneInd=0;
    private OPPIGraphics mGraphics=null;
    private long mStartTime=0l;
    private long mTotalTime=0l;
    private long mChapterStartTime=0l;
    private Chapter mCurrentChapter;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        // NOTE: API level 19 have immersive UI (ie. full screen that enables capturing all touch events)

        // TODO: Should buttons created dynamically? Should user be able to define view layout with json?
        // Now buttons are defined in the layout in resources, which makes easier to define layout for different devices
        // Customize: images to buttons, background color between spaces
        // OR create custom controlPane for this activity OR no buttons at all?
        setContentView( R.layout.activity_content );

        if( savedInstanceState == null ){
            Intent intent = getIntent();
            mPackageID = intent.getLongExtra("PACKAGE_ID", 0L);
            this.prepareContent( mPackageID );
        }

        Button back = (Button)findViewById( R.id.backButton );
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackWind();
            }
        });
        Button next = (Button)findViewById( R.id.nextButton );
        next.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onForwardWind();
            }
        });
        Button stop = (Button)findViewById( R.id.stopButton );
        stop.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onStop();
            }
        });
    }

    /**
     * Gets the first chapter and scene and prepares the audio for playback.
     * This method assumes that package is valid: it has chapters and scenes and valid data.
     *
     * @param packageID the packageId, which is shown to the user
     */
    public void prepareContent( long packageID ){
        /*
         TODO: create an AsyncTask to prepare this content taking the root directory
         from the Installed Modules data base
        */
        ContentPackage currentPackage = PackagePool.getInstance().getContent( packageID );
        // TODO: later chapter paging
        Chapter firstChapter = currentPackage.getChapter( 0 );
        mCurrentChapter = currentPackage.getChapter(0);
        Scene firstScene = firstChapter.getContentSceneAt(0);

        if( firstScene == null ){
            // PROBLEM!!! THROW EXCEPTION?? TODO: how to handle error situations...
        }
        mCurrentScene = firstScene;
        mAudioFile = firstChapter.getAudioName();
        // TODO: prepare audio

        Bitmap framebuffer = Bitmap.createBitmap( ((ContentScene) firstScene).getSceneWidth(), ((ContentScene) firstScene).getSceneHeight(), Bitmap.Config.RGB_565 );
        mAnimationSurface = new AnimationSurface( this, this, framebuffer );
        mGraphics = new OPPIGraphics( framebuffer, BitmapPool.getInstance() );

        FrameLayout contentFrame = (FrameLayout)findViewById( R.id.contentFrame );
        contentFrame.addView( mAnimationSurface );
    }

    /**
     * From OnPreparedListener
     * Callback when media source is ready for playback.
     *
     *
     * @param mediaPlayer
     */
    public void onPrepared( MediaPlayer mediaPlayer ){

        mAnimationSurface.startScene();
        ContentAudioPlayer.getInstance().playAudio( mAudioFile, getApplicationContext());
        mChapterStartTime = System.nanoTime();
    }


    /**
     * Method is called when user has pressed "BACK" button -> go to the previous scene
     * If this was first scene, DO nothing?
     *
     */
    @Override
    public void onBackWind() {
     //TODO It should restart the scene first time. Second should go to previous
        int audioMarkTime=0;
        // check if this is the first one
        ContentScene prevScene = (ContentScene)this.getPreviousScene();
        if( prevScene == null ){
            // if yes -> just start from beginning, seek audio to 0
            ContentScene currentScene = (ContentScene)mCurrentScene;
            audioMarkTime = currentScene.getStartTime();

        }else{
           audioMarkTime = prevScene.getStartTime();
           mCurrentSceneInd--;
           mCurrentScene = prevScene;
        }
        // TODO: perhaps we need to set OnSeekCompleteListener, and "pause" scene running until onSeekComplete is called...
        ContentAudioPlayer.getInstance().seekToPosition( (audioMarkTime) );
        mStartTime = 0l;
        mTotalTime = audioMarkTime;
        mCurrentScene.resume( this );
    }

     public void onStop() {

         ContentAudioPlayer.getInstance().release();
         super.onStop();
         finish();
    }

    /**
     * Method is called when user has pressed "NEXT" button -> go to next scene
     * If this was last scene, the go to "LAST SCENE" -> user is able to restart chapter or go to menu
     *
     */
    @Override
    public void onForwardWind() {
        int audioMarkTime=0;
        // check if this is the first one
        ContentScene nextScene = (ContentScene)this.getNextScene();
        if( nextScene == null ){
            // this is last, TODO: WHAT NEXT?
            ContentPackage currentPackage = PackagePool.getInstance().getContent( mPackageID );
            // go to end

        }else{
            audioMarkTime = nextScene.getStartTime();
            mCurrentSceneInd++;
            mCurrentScene = nextScene;
            ContentAudioPlayer.getInstance().seekToPosition( (audioMarkTime) );
        }

        mStartTime = 0l;
        mTotalTime = audioMarkTime;
        mCurrentScene.resume( this );
    }
    /**
     * Called when the whole chapter has been viewed.
     *
     *
     */
    public void onChapterEnd() {
        mAnimationSurface.stopScene();
        ContentAudioPlayer.getInstance().release();
        BitmapPool.getInstance().flush();
        finish();
    }
    /**
     * The core of the animationEngine logic. Calculates the scene, which suppose to be present at the moment.
     * TODO? Checks the chapter ends and audio synchronization.
     *
     * @return Scene current scene
     */
    @Override
    public Scene getCurrentScene() {
        //current playback time Milliseconds
        int elapsedMilliSeconds = ContentAudioPlayer.getInstance().getCurrentPosition();
        long elapsedNanoSeconds = elapsedMilliSeconds * 1000000;
        //if this is first time
        if( mStartTime == 0l ){
            mStartTime = System.nanoTime();
            Log.i(TAG, " New Scene Started at : "+ mStartTime/1000000000);
        }

        long deltaTime =(System.nanoTime()-mStartTime) / 1000000000;

        // get next scene and its startTime
        ContentScene nextScene = (ContentScene)this.getNextScene();
        //ContentScene nextScene = mCurrentChapter.getContentSceneAt(mCurrentSceneInd+1);

        if( nextScene != null ) { //TODO return a marker with "END SCENE"
            long sceneStartTime = nextScene.getStartTime();
            // if scene is done!
            if (elapsedMilliSeconds >= sceneStartTime) {
                // change Scene
                Log.i(TAG, " New Scene: " + nextScene.getJsonFile());
                mCurrentSceneInd++;
                mCurrentScene = nextScene;
                mCurrentScene.resume(this);
                mTotalTime += deltaTime;
                mStartTime = 0l;
            }
        }else{
            Log.d(TAG, "NO MORE SCENES");
            // COMMENTED BECAUSE THE TOTAL TIME IS NOT TRUSTWORTHY
            // this is last scene in this chapter
            // check if the scene chapter is over
  //          ContentPackage currentPackage = PackagePool.getInstance().getContent( mPackageID );
  //          long chapterDuration = currentPackage.getDuration();
  //          if( (mTotalTime + deltaTime) >= chapterDuration ){
  //              // YES -> GET "Last scene" or return to menu?
                if (!ContentAudioPlayer.getInstance().isPlaying()){
                     this.onChapterEnd();
                }
            }

        return mCurrentScene;
    }




    /**
     * Returns the next scene, does not update the current scene accounting.
     * TODO: This is static data, so it should be "easy" to ask: Which scene should be
     * playing at (x Time)?
     * @return next Scene, null if last scene
     */
    private Scene getNextScene(){
        Scene nextScene=null;
        int nextSceneIndex = mCurrentSceneInd+1;
        ContentPackage currentPackage = PackagePool.getInstance().getContent( mPackageID );
        Chapter currentChapter = currentPackage.getChapter( 0 ); //TODO: GET Current Chapter
        return currentChapter.getContentSceneAt(nextSceneIndex);
    }
    /**
     * Returns the previous scene, does not update the current scene accounting.
     *
     * @return previous Scene, null if first scene
     */
    private Scene getPreviousScene(){
        Scene previousScene=null;
        if( mCurrentSceneInd != 0 ){
            int previousSceneIndex = mCurrentSceneInd-1;
            ContentPackage currentPackage = PackagePool.getInstance().getContent( mPackageID );
            Chapter firstChapter = currentPackage.getChapter( 0 );
            previousScene = firstChapter.getContentSceneAt(previousSceneIndex);
        }
        return previousScene;
    }
    // TODO; are these required...
    // TODO: will we have "START SCENE"?
    public Scene getStartScene() {
        ContentPackage currentPackage = PackagePool.getInstance().getContent( mPackageID );
        Chapter firstChapter = currentPackage.getChapter( 0 );
        Scene firstScene = firstChapter.getContentSceneAt(0);
        return firstScene;
    }
    // TODO: will we have "END SCENE"
    public Scene getEndScene(){
        ContentPackage currentPackage = PackagePool.getInstance().getContent( mPackageID );
        Chapter firstChapter = currentPackage.getChapter( 0 );
        ArrayList<ContentScene> items = firstChapter.getAllScenes();
        Scene lastScene = items.get( items.size()-1 );
        return lastScene;
    }

    @Override
    public Graphics getGraphics() {
        return mGraphics;
    }
    // TODO: WHAT IF PAUSED -> START TIME IS THEN WRONG. MUST "HALTED" ON PAUSE!
    @Override
    public void onResume(){
        super.onResume();
        mAnimationSurface.resume();
        // === TESTIN PURPOSES =====
        this.onPrepared( null );
        // =========================
        mCurrentScene.resume( this );
        ContentAudioPlayer.getInstance().resume();
    }
    @Override
    public void onPause(){
        super.onPause();
        mAnimationSurface.pause();
        mCurrentScene.pause();
        ContentAudioPlayer.getInstance().pause();
        if ( isFinishing() ){
            mCurrentScene.dispose();
        }
    }
}
