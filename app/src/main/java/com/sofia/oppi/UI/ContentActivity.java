package com.sofia.oppi.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

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
import com.sofia.oppi.assets.ScreenAdapter;

import java.util.ArrayList;


/**
 *
 */
public class ContentActivity extends Activity implements AnimationEngine, SceneObserver, ScreenAdapter {
    private static final String TAG = "CONTENT_ACTIVITY";
    private AnimationSurface mAnimationSurface=null;
    private Long mPackageID;
    private String mAudioFile="";
    private Scene mCurrentScene=null;
    private int mCurrentSceneInd=0;
    private OPPIGraphics mGraphics=null;
    private long mStartTime=0l;
    private long mTotalTime=0l;

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
        /*stop.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onStop();
            }
        });*/
    }

    /**
     * Gets the first chapter and scene and prepares the audio for playback.
     * This method assumes that package is valid: it has chapters and scenes and valid data.
     *
     * @param packageID the packageId, which is shown to the user
     */
    public void prepareContent( long packageID ){
        ContentPackage currentPackage = PackagePool.getInstance().getContent( packageID );
        // TODO: later chapter paging
        Chapter firstChapter = currentPackage.getChapter( 0 );
        Scene firstScene = firstChapter.getItem( 0 );

        if( firstScene == null ){
            // PROBLEM!!! THROW EXCEPTION?? TODO: how to handle error situations...
        }
        mCurrentScene = firstScene;
        mAudioFile = firstChapter.getAudioName();
        // TODO: prepare audio

        Bitmap framebuffer = Bitmap.createBitmap( ((ContentScene) firstScene).getSceneWidth(), ((ContentScene) firstScene).getSceneHeight(), Bitmap.Config.RGB_565 );
        mAnimationSurface = new AnimationSurface( this, this, framebuffer );
        mGraphics = new OPPIGraphics( this, framebuffer, BitmapPool.getInstance() );

        FrameLayout contentFrame = (FrameLayout)findViewById( R.id.contentFrame );
        contentFrame.addView( mAnimationSurface );
    }

    /**
     * Called by View when android framework has finally created the view and measured the size.
     *
     * @param height
     * @param width
     */
    @Override
    public void onMeasure(int height, int width) {
/*
        Bitmap framebuffer = Bitmap.createBitmap( width, height, Bitmap.Config.RGB_565 );
        mAnimationSurface.attachFrameBuffer( framebuffer );
        mGraphics = new OPPIGraphics( this, framebuffer, BitmapPool.getInstance() );
*/

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

        //ContentAudioPlayer.getInstance().playAudio( mAudioFile, getApplicationContext());
    }


    /**
     * Method is called when user has pressed "BACK" button -> go to the previous scene
     * If this was first scene, DO nothing?
     *
     */
    @Override
    public void onBackWind() {

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
        ContentAudioPlayer.getInstance().seekToPosition( (audioMarkTime*1000) );
        mStartTime = 0l;
        mTotalTime = audioMarkTime;
        mCurrentScene.resume( this );
    }

/*    public void onStop() {

    }*/

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
            audioMarkTime = currentPackage.getDuration();
        }else{
            audioMarkTime = nextScene.getStartTime();
            mCurrentSceneInd++;
            mCurrentScene = nextScene;
        }
        ContentAudioPlayer.getInstance().seekToPosition( (audioMarkTime*1000) );
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
    }
    /**
     * The core of the animationEngine logic. Calculates the scene, which suppose to be present at the moment.
     * TODO? Checks the chapter ends and audio synchronization.
     *
     * @return Scene current scene
     */
    @Override
    public Scene getCurrentScene() {
        //if this is first time
        if( mStartTime == 0l ){
            mStartTime = System.nanoTime();
        }
        long deltaTime =(System.nanoTime()-mStartTime) / 1000000000;

        // get next scene and its startTime
        ContentScene nextScene = (ContentScene)this.getNextScene();
        if( nextScene != null ){
            long sceneStartTime = nextScene.getStartTime();
            // if scene is done!
            if( (mTotalTime + deltaTime) >= sceneStartTime ){
                // change Scene
                mCurrentSceneInd++;
                mCurrentScene = nextScene;
                mCurrentScene.resume( this );
                mTotalTime += deltaTime;
                mStartTime = 0l;
            }
        }else{
            // this is last scene in this chapter
            // check if the scene chapter is over
            ContentPackage currentPackage = PackagePool.getInstance().getContent( mPackageID );
            long chapterDuration = currentPackage.getDuration();
            if( (mTotalTime + deltaTime) >= chapterDuration ){
                // YES -> GET "Last scene" or return to menu?
                this.onChapterEnd();
            }
        }
        return mCurrentScene;
    }
    /**
     * Returns the next scene, does not update the current scene accounting.
     *
     * @return next Scene, null if last scene
     */
    private Scene getNextScene(){
        Scene nextScene=null;
        int nextSceneIndex = mCurrentSceneInd+1;
        ContentPackage currentPackage = PackagePool.getInstance().getContent( mPackageID );
        Chapter firstChapter = currentPackage.getChapter( 0 );
        return firstChapter.getItem( nextSceneIndex );
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
            previousScene = firstChapter.getItem( previousSceneIndex );
        }
        return previousScene;
    }
    // TODO; are these required...
    // TODO: will we have "START SCENE"?
    public Scene getStartScene() {
        ContentPackage currentPackage = PackagePool.getInstance().getContent( mPackageID );
        Chapter firstChapter = currentPackage.getChapter( 0 );
        Scene firstScene = firstChapter.getItem( 0 );
        return firstScene;
    }
    // TODO: will we have "END SCENE"
    public Scene getEndScene(){
        ContentPackage currentPackage = PackagePool.getInstance().getContent( mPackageID );
        Chapter firstChapter = currentPackage.getChapter( 0 );
        ArrayList<ContentScene> items = firstChapter.getAllItems();
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

    @Override
    public int getViewHeight() {
        int height=0;
        if( mAnimationSurface != null ){
            height=mAnimationSurface.getMeasuredHeight();
        }
        return height;
    }
    @Override
    public int getViewWidth() {
        int width=0;
        if( mAnimationSurface != null ){
            width=mAnimationSurface.getMeasuredWidth();
        }
        return width;
    }
}
