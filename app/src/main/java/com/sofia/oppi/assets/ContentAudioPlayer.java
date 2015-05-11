package com.sofia.oppi.assets;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;



import java.io.IOException;

/**
 */
public class ContentAudioPlayer {
    private static ContentAudioPlayer instance;
    private MediaPlayer mPlayer=null;

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
        mPlayer = MediaPlayer.create(context, Uri.parse(audioName) );
        mPlayer.start();
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
}
