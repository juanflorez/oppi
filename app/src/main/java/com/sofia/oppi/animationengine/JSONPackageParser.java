package com.sofia.oppi.animationengine;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * Gets content specification as JSON and parses it and returns required PackageItem
 * TODO: different versions of parser also?
 */

public class JSONPackageParser {
    final private String TAG="JSONContentParser";

    /**
     * parses the contentpackage.json
     *
     * @param reader
     */
    public ContentPackage parsePackage( JsonReader reader ) {
        ContentPackage contentpackage=null;
        reader.setLenient( true );
        try{
            reader.beginObject();

            while( reader.hasNext() ){
                String elementName = reader.nextName();
                if( elementName.equalsIgnoreCase( "packagedata" ) ){
                    // read package data
                    contentpackage = readPackageInfo( reader );

                }else if( elementName.equalsIgnoreCase( "desktop") ){
                    // TODO: should this information added to package
                    reader.skipValue();
                }else{
                    // TODO: throw exception, not valid packageJson
                    reader.skipValue();
                }
            }
            reader.endObject();

        }catch( IOException e ) {

            Log.e( TAG, e.toString() );
        }

        return contentpackage;
    }

    /**
     * Method for getting the PackageData" element from JSON
     * @param reader
     */
    private ContentPackage readPackageInfo( JsonReader reader ) throws IOException {
        String id="";
        String name="";
        String packversion="";
        String engversion="";
        String minres="";
        String maxres="";
        ContentPackage pack=null;
        // TODO; test if nextlong etc. works...did not work earlier with hardcoded jsons

        reader.beginObject();
        while( reader.hasNext() ){
            String elementName = reader.nextName();

            if( elementName.equalsIgnoreCase( "packageid" ) ){
                id=reader.nextString();
            }else if( elementName.equalsIgnoreCase( "name" ) ){
                name=reader.nextString();
            }else if( elementName.equalsIgnoreCase( "packageversion" ) ){
                packversion=reader.nextString();
            }else if( elementName.equalsIgnoreCase( "engineversion") ){
                engversion=reader.nextString();
            }else if( elementName.equalsIgnoreCase( "minimumresolution") ){
                minres=reader.nextString();
            }else if( elementName.equalsIgnoreCase( "maximumresolution") ){
                maxres=reader.nextString();
            }else{
                // TODO: should throw exception - wrong kind of schema?!
                reader.skipValue();
            }
        }
        if( name.length() != 0 && id.length() != 0 && packversion.length() != 0 && engversion.length() != 0
                && minres.length() != 0 && maxres.length() != 0 ){
            pack = new ContentPackage( name, Long.parseLong(id), packversion,engversion,minres,maxres );
        }
        reader.endObject();

        return pack;
    }

    /**
     * Method for parsing all json of chapter-type
     *
     * @param reader
     * @return
     */
    // TODO: how to distinguish chapter-json? There is no refer to chapter in the content.json and
    // TODO: chapter directory includes also scene -jsons
    public Chapter parseChapter( JsonReader reader )  {
        Chapter chapter= new Chapter();
        String audioName="";
        ArrayList<Scene> sceneArray=null;
        reader.setLenient( true );
        try{
            reader.beginObject();
            while( reader.hasNext() ){
                String elementName = reader.nextName();
                if( elementName.equalsIgnoreCase( "soundfile" ) ){
                    // read package data
                    audioName = reader.nextString();

                }else if( elementName.equalsIgnoreCase( "scenes") ){
                    reader.beginArray();
                    while( reader.hasNext() ){
                        reader.beginObject();
                        parseScene( reader, chapter );
                        reader.endObject();
                    }
                    reader.endArray();
                }else{
                    // TODO: throw exception, not valid packageJson?
                    reader.skipValue();
                }
            }
            reader.endObject();

        }catch( IOException e ) {

            e.printStackTrace();
        }
        return chapter;
    }

    /**
     *
     * @param reader
     * @return
     */
    private void parseScene( JsonReader reader, Chapter chapter ) throws IOException{
        String sceneFile="";
        String startTime="";
        String background="";
        String screenwidth="";
        String screenheight="";

        while( reader.hasNext() ){

            String elementName = reader.nextName();
            if( elementName.equalsIgnoreCase( "scenefile" ) ){
                // read package data
                sceneFile = reader.nextString();

            }else if( elementName.equalsIgnoreCase( "startTime") ){
                startTime = reader.nextString();
            }else if( elementName.equalsIgnoreCase( "background" ) ){
                background = reader.nextString();
            }
            else if( elementName.equalsIgnoreCase( "screenwidth")){
                screenwidth = reader.nextString();
            }
            else if( elementName.equalsIgnoreCase( "screenhight") ){
                screenheight = reader.nextString();
            }
            else{
                // TODO: throw exception, not valid packageJson
                reader.skipValue();
            }
            if( sceneFile.length() != 0 && startTime.length() != 0 && background.length() != 0 &&
                    screenheight.length() != 0 && screenwidth.length() != 0 ){
                Scene newScene = new Scene( sceneFile, startTime, background, screenheight, screenwidth );
                chapter.add( newScene );
            }
        }
    }

    /**
     *
     * @param reader
     * @return
     */
    public ArrayList<PackageItem> parseFrameInfo( JsonReader reader ) {
        ArrayList<PackageItem> frames=new ArrayList<PackageItem>();

        reader.setLenient( true );
        try{
            reader.beginObject();
            while( reader.hasNext() ){
                String frameName = reader.nextName(); // TODO: is name required...
                reader.beginObject();
                Frame frame = parseFrame( reader );
                frames.add( frame );
                reader.endObject();
            }
            reader.endObject();

        }catch( IOException e ){
            e.printStackTrace();
        }
        return frames;
    }

    /**
     *
     * @param reader
     * @return
     * @throws IOException
     */
    private Frame parseFrame(JsonReader reader) throws IOException{
        String duration="";
        ArrayList<ImageItem> items=null;
        Frame frame=null;

        while( reader.hasNext() ){
            String elementName = reader.nextName();

            if( elementName.equalsIgnoreCase( "duration" ) ){
                duration = reader.nextString();

            }else if( elementName.startsWith( "images") ){
                reader.beginArray();
                items=new ArrayList<ImageItem>();
                while( reader.hasNext() ){
                    reader.beginObject();
                    ImageItem image=parseImageItem( reader );
                    items.add( image );
                    reader.endObject();
                }
                reader.endArray();
            }else{
                // TODO: throw exception, not valid packageJson
                reader.skipValue();
            }
            if( duration.length() != 0 && items != null ){
                 frame = new Frame( duration, items );
            }
        }
        return frame;
    }

    /**
     *
     * @param reader
     * @return
     */
    private ImageItem parseImageItem( JsonReader reader ) throws IOException{
        ImageItem image=null;
        String fileName="";
        String posX="";
        String posY="";

        while( reader.hasNext() ){
            String elementName = reader.nextName();
            if( elementName.equalsIgnoreCase( "file" )){
                fileName=reader.nextString();
            }else if( elementName.equalsIgnoreCase( "pos_x" )){
                posX=reader.nextString();
            }else if( elementName.equalsIgnoreCase( "pos_y" )){
                posY=reader.nextString();
            }else{
                // TODO: not valid!
                reader.skipValue();
            }
            if( fileName.length() != 0 && posX.length() != 0 && posY.length() != 0 ){
                image = new ImageItem( fileName, Integer.parseInt(posX), Integer.parseInt(posY) );
            }
        }
        return image;
    }
}
