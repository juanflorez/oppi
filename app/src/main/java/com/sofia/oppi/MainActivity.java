package com.sofia.oppi;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sofia.oppi.animationengine.Chapter;
import com.sofia.oppi.animationengine.ContentPackage;
import com.sofia.oppi.animationengine.Frame;
import com.sofia.oppi.animationengine.JSONPackageParser;
import com.sofia.oppi.animationengine.PackageItem;
import com.sofia.oppi.animationengine.Scene;
import com.sofia.oppi.content.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    final private String TAG="MainActivity";
    PackagePool mPackagePool =null;
    JSONPackageParser mContentParser=null;
    //AnimationEngine mAnimationEngine=null; // all versions implements AnimationEngine interface

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ContentListFragment())
                    .commit();
        }

        // create all types of animation engines, USE ABSTRACTFACTORY PATTERN?

        // create bitmapfactor

        mPackagePool = new PackageManager();
        // create animationparser
        mContentParser = new JSONPackageParser();

        this.parseResources();

    }

    /**
     * FOR TESTING PURPOSES. PARSES THE JSON-FILES IN RESOURCES
     * THIS WILL CHANGE WHEN JSON IS GOT FROM SERVER!
     */
    private void parseResources(){

        // NOTE:
        // IN THE TEST PHASE, The easiest way to test jsons is to add them to the resources
        // -> files (which are not in the resources) are saved either extenal storage or as private to
        // folder data/data/packagename/files/... This directory is NOT under app sources and
        // not visible when debugging/with emulator...Same applies to image-files.
        // CORRECT if wrong!!!
        // ANOTHER OPTION IS main/assets/ BUT THESE ARE ALSO COMPILED TO APK!!

        // TODO: will user download only whole chapters?

        ContentPackage contentPackage=null;
        Chapter chapter=null;

        int[] resourcesToRead = {R.raw.content, R.raw.chapter1, R.raw.first, R.raw.second, R.raw.third };


        for( int i=0; i < resourcesToRead.length; i++ ){
            InputStream jsonPackageStream = getResources().openRawResource( resourcesToRead[i] );
            String resourceName = getResources().getResourceEntryName( resourcesToRead[i] );

            JsonReader jsonPackageReader=null;
            try{
                jsonPackageReader = new JsonReader( new InputStreamReader( jsonPackageStream, "UTF-8" ) );
                if( resourcesToRead[i] == R.raw.content ) {
                    contentPackage = mContentParser.parsePackage( jsonPackageReader );
                }else if( resourcesToRead[i] == R.raw.chapter1 ){
                    chapter = mContentParser.parseChapter( jsonPackageReader );
                }else{
                    // this is frame!
                    ArrayList<PackageItem> frames = mContentParser.parseFrameInfo( jsonPackageReader );

                    // TODO: figure out better way to link these files!!!
                    if( chapter != null ){
                        ArrayList<PackageItem> items = chapter.getAllItems();
                        for( PackageItem item : items ){
                            Scene scene = (Scene)item;

                            if( scene.getJsonFile().contains( resourceName ) ){
                                scene.add( frames );
                                break;
                            }
                        }
                    }
                }
            }catch( IOException e ){
                Log.e( TAG, e.toString() );
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A fragment for showing the available contents.
     * TODO: separate file?
     */
    public static class ContentListFragment extends Fragment {

        public ContentListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_contentlist, container, false);

            // TODO: get the list of available contents from ContentPool show them, let usr select

            // TODO: check the version:
/*
            try{
                String version = firstTestContent.getString( "version");

                if( version.equalsIgnoreCase( "0.1") ){
                    // get the right type of animation engine
                    //mAnimationEngine = AnimationEngineFactory.getEngine( version );
                }
            }catch( JSONException e ){
                Log.e( TAG, e.toString() );
            }
*/

            return rootView;
        }
    }
}
