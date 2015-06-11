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
import com.sofia.oppi.animationengine.ContentChapter;
import com.sofia.oppi.animationengine.ContentPackage;
import com.sofia.oppi.animationengine.Graphics;
import com.sofia.oppi.animationengine.OPPIGraphics;
import com.sofia.oppi.animationengine.Scene;
import com.sofia.oppi.animationengine.SceneObserver;
import com.sofia.oppi.assets.BitmapPool;
import com.sofia.oppi.assets.ContentAudioPlayer;
import com.sofia.oppi.assets.PackagePool;


/**
 *
 */
public class ContentActivity extends Activity implements AnimationEngine, SceneObserver{
    private static final String TAG = "CONTENT_ACTIVITY";
    private AnimationSurface mAnimationSurface=null;
    private Long mPackageID;
    private Scene mCurrentScene=null;
    private int mCurrentSceneInd=0;
    private OPPIGraphics mGraphics=null;

    private ContentChapter mCurrentChapter;
    private int     mCurrentChapterInd=0;
    private int     mNextChapterInd = 0;
    private Chapter mNextChapter;
    private ContentPackage mCurrentPackage;
    private int mTotalChapScenes = 0;

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
        if( savedInstanceState == null ){
            Intent intent = getIntent();
            mPackageID = intent.getLongExtra("PACKAGE_ID", 0L);
            this.prepareContent( mPackageID, 0 );
        }


    }

    /**
     * Gets the first chapter and scene and prepares the audio for playback.
     * This method assumes that package is valid: it has chapters and scenes and valid data.
     *
     * @param packageID the packageId, which is shown to the user
     * @param chapterInd the chapter from which it will start
     */
    public void prepareContent( long packageID, int chapterInd ){

        mCurrentPackage = PackagePool.getInstance().getContent( packageID );
        mCurrentSceneInd = 0;
        mCurrentScene = mCurrentPackage.getChapter(chapterInd).getSceneAt(mCurrentSceneInd);
        Bitmap framebuffer = Bitmap.createBitmap(  mCurrentScene.getSceneWidth(),
                mCurrentScene.getSceneHeight(), Bitmap.Config.RGB_565 );
        mAnimationSurface = new AnimationSurface( this, this, framebuffer );
        mGraphics = new OPPIGraphics( framebuffer, BitmapPool.getInstance() );
        FrameLayout contentFrame = (FrameLayout)findViewById( R.id.contentFrame );
        contentFrame.addView( mAnimationSurface );
        mAnimationSurface.startScene();
        jumpToChapter(chapterInd, mCurrentSceneInd);

    }

    /**
     * From OnPreparedListener
     * Callback when media source is ready for playback.
     *
     *
     * @param mediaPlayer
     */
    //TODO do we need the media player here?
    public void onPrepared( MediaPlayer mediaPlayer ){

        jumpToChapter(mCurrentChapterInd, mCurrentSceneInd);

    }

    /**
     *
      * @param chapterInd
     * @param sceneInd
     * @return The current scene.
     */
    private Scene jumpToChapter(int chapterInd, int sceneInd) {

        if( mCurrentPackage.getChapter(chapterInd) != null) {
            // TODO THERE ARE ONLY CONTENT CHAPTERS it does not change scene if the next chapter is null
            ContentChapter tmpCh = (ContentChapter)mCurrentPackage.getChapter(chapterInd);
            if (tmpCh == null) {
                return mCurrentScene;
            }
            mCurrentChapterInd = chapterInd;
            mCurrentChapter = (ContentChapter)mCurrentPackage.getChapter(mCurrentChapterInd);
            mCurrentSceneInd = sceneInd;
            mNextChapterInd = mCurrentChapterInd + 1;
            mNextChapter = mCurrentPackage.getChapter(mNextChapterInd);
            mCurrentChapter.startPlayback(this, sceneInd,this);
            mCurrentScene = mCurrentChapter.getCurrentScene(); // HERE IS ALL THE LOGIC
            mTotalChapScenes = mCurrentChapter.getTotalScenes();

        }
          return mCurrentScene;

    }

    /**
     * Method is called when user has pressed "BACK" button -> go to the previous scene
     * If this was first scene, start again
     *
     */
    @Override
    public int onBackWind() {
     // if current scene is 0, go to previous chapter
        if (mCurrentSceneInd == 0){
            ContentChapter contentChapter =(ContentChapter) mCurrentPackage.getChapter(mCurrentChapterInd-1);
            if (contentChapter != null){
                int lastScene = contentChapter.getTotalScenes()-1;
                jumpToChapter(mCurrentChapterInd-1,lastScene);
                return mCurrentSceneInd;
            } else { // we are at the begining of the package

                jumpToChapter(0,0);
                return mCurrentSceneInd;

            }

        }
        // check if this is the first one
        Scene prevScene = mCurrentChapter.getActionSceneAt(mCurrentSceneInd-1);
        if( prevScene == null ){
            mCurrentChapter.startPlayback(this, mCurrentSceneInd,this);
        }else{
           mCurrentSceneInd--;
           mCurrentScene = prevScene;
           mCurrentChapter.startPlayback(this, mCurrentSceneInd,this);
        }
        return mCurrentSceneInd;
    }

     public void onStop() {

         mCurrentChapter.stopPlayback();
         super.onStop();
         finish();
    }

    /**
     * Method is called when user has pressed "NEXT" button -> go to next scene
     * If this was last scene, go to next chapter. or do nothing if it is the final one
     * @return currentSceneInd
     *
     */
    @Override
    public int onForwardWind() {

        jumpToChapter(mCurrentChapterInd,mCurrentSceneInd+1);
        return mCurrentSceneInd;

    }
    /**
     * Called when the whole chapter has been viewed.
     *
     *
     */
    public void onModuleEnd() {
        mAnimationSurface.stopScene();
        ContentAudioPlayer.getInstance().release();
        BitmapPool.getInstance().flush();
        finish();
    }
    /**
     * The core of the animationEngine logic. Calculates the scene, which suppose to be present at the moment.
     * TODO? Checks the chapter ends and audio synchronization.
     * TODO THE JUMPING SHOULD BE DONE IN THE CONTROL METHOD. THIS METHOD SHOULD ONLY
     * RETURN THE CURRENT SCENE.
     *
     * @return Scene current scene, or tirggers moduleEnd if the playback ends.
     */
    @Override
    public Scene getCurrentScene() {
        //TODO: By now it changes chapter once it gets a null scene. Improve it using an Observer
        //to know when scene has changed THIS IS UGLY!!!
        Log.d(TAG, "currentScInd "+ mCurrentSceneInd + " TotalScnes "+ mTotalChapScenes);

            Scene currentScene = mCurrentChapter.getCurrentScene();

        if (currentScene != null) {

            mCurrentScene = currentScene;
            mCurrentSceneInd = mCurrentChapter.getCurrentSceneInd();
            Log.d(TAG, "Current _Sc "+ mCurrentScene.getJsonFile());
            return mCurrentScene;
        } else {

            Log.d(TAG, "Last _Sc "+ mCurrentScene.getJsonFile());
            return jumpToChapter(mNextChapterInd,0);
        }

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
        //this.onPrepared( null );
        // =========================
        mCurrentScene.resume( this );
        ContentAudioPlayer.getInstance().resume();
    }
    @Override
    public void onPause(){
        super.onPause();
        mAnimationSurface.pause();
        mCurrentScene.pause();
        ContentAudioPlayer.getInstance().pause(); //TODO Chapter is the responsible to pause
        if ( isFinishing() ){
            mCurrentScene.dispose();
        }
    }
}
