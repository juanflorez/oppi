package com.sofia.oppi.UI;

import android.content.Intent;
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
import android.widget.Button;

import com.sofia.oppi.animationengine.ContentScene;
import com.sofia.oppi.assets.BitmapPool;
import com.sofia.oppi.R;
import com.sofia.oppi.animationengine.Chapter;
import com.sofia.oppi.animationengine.ContentPackage;
import com.sofia.oppi.animationengine.Frame;
import com.sofia.oppi.animationengine.JSONPackageParser;
import com.sofia.oppi.assets.PackagePool;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


// TODO: support ONLY LANDSCAPE!!!


public class MainActivity extends ActionBarActivity {
    final private String TAG="MainActivity";
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

        // create package parser
        mContentParser = new JSONPackageParser();



        this.parseResources();
        // FOR TESTING purposes, later get bitmaps from server. now from resources
        BitmapPool.getInstance().loadImages( this );

        final Button testButton = (Button)findViewById( R.id.testButton );
        testButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLesson();
            }
        });

    }
    public void startLesson(){
        // TODO: add one button here to start the new activity for testing purposes
        // LATER USER SELECTS THE PACKAGE SHE WANTS TO SEE FROM THE LIST.
        Intent intent  = new Intent( this, ContentActivity.class );
        intent.putExtra( "PACKAGE_ID", (long)22150321 );
        startActivity(intent); // calls pause for this activity!
    }

    /**
     * FOR TESTING PURPOSES. PARSES THE JSON-FILES IN RESOURCES
     *
     */
    private void parseResources(){

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
                    contentPackage.addChapter( chapter );
                }else{
                    // this is frame!
                    ArrayList<Frame> frames = mContentParser.parseFrameInfo( jsonPackageReader );

                    // TODO: figure out better way to link these files!!!
                    if( chapter != null ){
                        ArrayList<ContentScene> items = chapter.getAllItems();
                        for( ContentScene item : items ){
                            ContentScene sceneScreen = item;

                            if( sceneScreen.getJsonFile().contains( resourceName ) ){
                                sceneScreen.add( frames );
                                break;
                            }
                        }
                    }
                }
            }catch( IOException e ){
                Log.e( TAG, e.toString() );
            }
        }
        if( contentPackage != null ){
            PackagePool.getInstance().addContent( contentPackage );
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
