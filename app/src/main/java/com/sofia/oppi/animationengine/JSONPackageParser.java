package com.sofia.oppi.animationengine;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * Gets content specification as JSON and parses it and returns required PackageItem
 * TODO: different versions of parser also?
 * TODO: JSONObject may help here?
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

                }else if( elementName.equalsIgnoreCase( "desktop") ) {
                    // TODO: should this information added to package
                    reader.skipValue();
                    // TODO: get the duration and other information
                    contentpackage.setDuration( "00:00:37");
                }else if( elementName.equalsIgnoreCase( "chapters")){
                     // hold that array in the chaptersArray by now.
                     makeChapterArray( reader );
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

    //TODO: Decide where to store the list of chapters.
/**
 * The content will be divided in chapters.
 * A chapter is linked to 1 media file, and can have one or
 * multiple scenes. the chapter array will tell the engine which chapters to play in which order.
 * */
    private void makeChapterArray(JsonReader reader) throws IOException {

        String fileName="";
        ArrayList<String> chapterFiles=null;

        while (reader.hasNext() ){
            String elementName = reader.nextName();
            if( elementName.equalsIgnoreCase("fileName") ){
                fileName = reader.nextString();
                chapterFiles.add(fileName);
                Log.v(TAG, "Adding " + fileName);
            }
        }

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
        //TODO: I am not sure if all of the fields are mandatory

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
        ArrayList<ContentScene> sceneScreenArray =null;
        reader.setLenient( true );
        try{
            reader.beginObject();
            while( reader.hasNext() ){
                String elementName = reader.nextName();
                if( elementName.equalsIgnoreCase( "soundfile" ) ){
                    // read package data
                    audioName = reader.nextString();
                    chapter.setAudioName( audioName );

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
                ContentScene newSceneScreen = new ContentScene( sceneFile, startTime, background, screenheight, screenwidth );
                chapter.add( newSceneScreen) ;
            }
        }
    }

    /**
     *
     * @param reader
     * @return
     */
    public ArrayList<Frame> parseFrameInfo( JsonReader reader ) {
        ArrayList<Frame> frames=new ArrayList<Frame>();

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
        ArrayList<FrameImage> items=null;
        Frame frame=null;

        while( reader.hasNext() ){
            String elementName = reader.nextName();

            if( elementName.equalsIgnoreCase( "duration" ) ){
                duration = reader.nextString();

            }else if( elementName.startsWith( "images") ){
                reader.beginArray();
                items=new ArrayList<FrameImage>();
                while( reader.hasNext() ){
                    reader.beginObject();
                    FrameImage image=parseImageItem( reader );
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
    private FrameImage parseImageItem( JsonReader reader ) throws IOException{
        FrameImage image=null;
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
                image = new FrameImage( fileName, Integer.parseInt(posX), Integer.parseInt(posY) );
            }
        }
        return image;
    }
}
