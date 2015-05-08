package com.sofia.oppi.animationengine;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sofia.oppi.dbUtils.StringXtractor;

import java.util.ArrayList;

/**
 * Created by juanflorez on 04/05/15.
 * Given a file path, get the content.json and create a full
 * ContentPackage object
 */
public class ModuleGsonParser {

    private static final String TAG = "ModuleGsonParser" ;

    public static ContentPackage getContentPackage(String pathToFile) throws Exception{

        String contentString ="";
        try {
            contentString = StringXtractor.getStringFromFile(pathToFile+"content.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ContentPackage thePackage;
        Gson gson = new Gson();
        thePackage = gson.fromJson(contentString, ContentPackage.class);

        if(!thePackage.chapters.isEmpty()) {
           ArrayList<ContentPackage.ChapterName> array = thePackage.chapters;
           for(int i = 0; i< array.size(); i++)
           {
               String chapterJsonString =
                          StringXtractor.getStringFromFile(pathToFile+
                                  "/chapters/"+
                          array.get(i).getFileName());
               Chapter chapter = gson.fromJson(chapterJsonString,Chapter.class);


               // parse the frames for each  scene file in the chapter
               ArrayList<ContentScene> sceneArray = chapter.getAllScenes();

               for (int j = 0; j< sceneArray.size(); j++)
               {
                   String ScenesJsonString =
                           StringXtractor.getStringFromFile(pathToFile +
                                  "/chapters/"+
                            sceneArray.get(j).getJsonFile()
                           );
                   Log.d(TAG, "parsing "+sceneArray.get(j).getJsonFile() );
                   Log.d(TAG, ScenesJsonString);
                   ArrayList<Frame> tmpFramesArray = gson.fromJson(ScenesJsonString,
                                                    new TypeToken<ArrayList<Frame>>(){}
                                                        .getType());

                   Log.d(TAG, "Collection size  " + tmpFramesArray.size());
                   chapter.getContentSceneAt(j).setFrames(tmpFramesArray);

               }
               chapter.setRoot(pathToFile);
               thePackage.addChapter(chapter);

           }

        }

        Log.i(TAG,thePackage.toString());
        Log.d(TAG,thePackage.getChapter(0).getContentSceneAt(0).getFrames()
                .get(0).getImages().get(0).getBitmapName() );
        thePackage.setRoot(pathToFile);
        return thePackage;

    }
    //TODO Especific Exception for this case

    public static int getMilliSeconds(String duration)throws Exception {
        String times[] = duration.split(":",3);
        int result = 14000;
        if (times.length != 3){
            Exception ex = new Exception("The format for time is incorrect: " + duration);
            throw ex;
        }

        int hours = Integer.parseInt(times[0]);
        int minutes = Integer.parseInt(times[1]);
        int seconds = Integer.parseInt(times[2]);


        try {

            result = seconds * 1000 +
                    minutes * 1000 * 60 +
                    hours * 1000 * 60 * 60;

        }catch (Exception e) {
            throw e;
        }

            return result;
    }

}
