package com.sofia.oppi.content;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Class for managing content in JSONObject format. When new content is received from server, ContentManager deposits the content.
 * When app is closed, it saves all the downloaded contents to the file as JSONObjects. Implements the ContentPool for
 * acquiring content by ID/name or getting the list of downloaded Contents.
 * If ContentManager does not have the acquired Content, it sends request to the server (ie. serverPacakage) to download the Content. -> TODO: contentLoaderObserver
 */
// TODO: when content related audios and bitmaps are downloaded? Those one which are app included (in the resources) are created on app start?
public class PackageManager implements PackagePool {
    // TODO: arrayList of what...?
    ArrayList<JSONObject> mContents;
    final private String TAG="LESSON";

    public void ContentManager(){
        mContents = new ArrayList<JSONObject>();
        this.createContentTEST();
    }

    /**
     * For testing pursposes, creates JSONObjects from literal notations..later get these from server
     */
    private void createContentTEST(){
        // TODO: we could have separate JSON to just describe ContentManager; those lessons could be shown then on the main view
        // and user could select the ContentManager on the basis of the description?
        // like: name, description, thumbnail, chargeable etc.
        JSONObject numbersContent=null;
        try{
            // Learn-section is the phase when content is introduced to the user. Does not have (at least in the phase)
            // interaction (expect navigation). Learn section consists of screens, which are shown sequentially. user can also navigate between screens by pressing buttons /forward/backward).
            // Screen have list of bitmaps (not animated in this phase)
            // Learn section generally have audio and background (bitmap or color). Learn section's background is shown if screen does not define its own background
            // version: which version of animationEngine we use (ie. how the animation is implemented and how the content is parsed)
            // correctItems list include items that user should click in the Try-part. When all the items in the list have been clicked,
            // show ends and user can either rerun the content or select new one from the list

            // Try section is an interactive:
            // it consist of one screen, which have multiple items.
            // Items have bitmaps and actions, which are run when user clicks the item.
            numbersContent = new JSONObject( "{'version': '0.1', 'name': 'BlueColor', 'description': 'This is very fun way of learn colors (and numbers)!', " +
                    "'learnSection': " +
                    "{'audio': 'counter.mp3', 'backgroundColor': '255,255,255'" +
                    "'screens': " +
                    "[" +
                    " {'order': '1','duration': '3','bitmaps': [{'name': 'zero.png', 'position':'100,100'},{'name':'one.png','position':'200,200'},{'name':'two.png','position':'300,300'}]}," +
                    " {'order': '2','duration': '3','bitmaps': [{'name':'three.png','position':'100,100'},{'name':'four.png', 'position':'200,200'},{'name':'five.png','position':'300,300'}]}" +
                    " {'order': '3','duration': '4','bitmaps': [{'name':'six.png','position':'100,100'},{'name':'seven.png', 'position':'200,200'},{'name':'eight.png','position':'300,300'},{'name':'nine.png','position':'400,400'}]}" +
                    "]}" +
                    "'trySection': " +
                    "{'correctItems': ['eightBlue','oneBlue'], 'items':} " +
                    "[" +
                    " {'name':'eightBlue','bitmap':'eight.png', 'position':'100,100', 'actionOnClick':{'action':'PULSE','sound':'blop.mp3',}" + // sound could ne then "Great.mp3"
                    " {'name':'oneBlue','bitmap':'one.png', 'position':'400,400', 'actionOnClick':{'action':'PULSE','sound':'blop.mp3',}" + // sound could ne then "Great.mp3"
                    " {'name':'zeroPurple','bitmap':'zero.png', 'position':'200,200', 'actionOnClick':{'action':'SHAKE','sound':'flyby.mp3',}" + // sound could be "oho.mp3"
                    "}");

            mContents.add( numbersContent );

        }catch ( JSONException exception ){
            Log.e(TAG, exception.getMessage());
        }
    }

    @Override
    public JSONObject getContent( String name ) {
        JSONObject content=null;
        // later get more "sophisticated" way of getting the correct content
        if( name.equalsIgnoreCase("bluecolor") ){
            content = mContents.get( 0 );
        }
        return content;
    }

    @Override
    public JSONArray getAvailableContentsList() {
        return null;
    }
}
