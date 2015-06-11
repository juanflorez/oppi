package com.sofia.oppi.assets;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;


import java.io.IOException;

/**
 */
public class ContentAudioPlayer implements MediaPlayer.OnCompletionListener{
    private static final String TAG = "Cont_Audio_Pl";
    private static ContentAudioPlayer instance;
    private MediaPlayer mPlayer=null;
    private boolean mCompleted = false;

    private ContentAudioPlayer(){
    }

    public static synchronized ContentAudioPlayer getInstance(){
        if( instance == null ){
            instance = new ContentAudioPlayer();
        }
        return instance;
    }

    private void prepareAudio( Context context, String uri ){

       Uri fileUri = Uri.parse( uri );
       mPlayer = new MediaPlayer();

        try{
            mPlayer.setDataSource(context, fileUri);
        }catch( IOException e ){
            e.printStackTrace();
        }

    }

    public void playAudio( String audioName, Context context ){
        if(mPlayer!=null && mPlayer.isPlaying()){
            mPlayer.release();
        }
        mPlayer = MediaPlayer.create(context, Uri.parse(audioName) );
        mPlayer.setOnCompletionListener(this);
        mPlayer.start();
    }

    /**
     *
     * @param audioFilePAth full path for the audio file
     * @param context
     * @param milliseconds Start point
     * @return
     */
    public boolean prepare (String audioFilePAth, Context context, int milliseconds){

        if(mPlayer != null && mPlayer.isPlaying()){
            mPlayer.stop();
            mPlayer.release();
        }

        if(mPlayer != null){
            mPlayer.release();
        }

        mPlayer = new  MediaPlayer();

        try {
            mPlayer.setDataSource(context, Uri.parse(audioFilePAth));
            mPlayer.prepare();
            mPlayer.seekTo(milliseconds);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            // CATASTROPHIC FAIL
            return false;
        }

    }

    public  void startPlaying () {
        if(mPlayer != null && !mPlayer.isPlaying()){
            mPlayer.start();
        }
    }


    public int getDuration(){
        int duration=0;
        if( mPlayer != null ){
            duration = mPlayer.getDuration();
        }
        return duration;
    }

    public int getCurrentPosition(){
        int position=0;
        if( mPlayer != null ){
            position = mPlayer.getCurrentPosition();
        }
        return position;
    }

    public boolean playbackCompleted(){
        return mCompleted;
    }

    public void pause(){
        if( mPlayer != null && mPlayer.isPlaying() ){
            mPlayer.pause();
        }
    }

    public void resume(){
        if( mPlayer != null && mPlayer.isPlaying() == false ){
            mPlayer.start();
        }
    }

    public boolean isPlaying(){
        boolean status=false;
        if( mPlayer != null && mPlayer.isPlaying() == true ){
            status = true;
        }
        return status;
    }

    public void seekToPosition( int audioPosition ){
        if( mPlayer != null ){
            mPlayer.seekTo( audioPosition );
        }
    }

    public void release(){
        if(mPlayer!=null && mPlayer.isPlaying()){
            mPlayer.stop();
        }

        if(mPlayer!=null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mCompleted = true;
        Log.d(TAG, "Playback Completed");
    }
}
