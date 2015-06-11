package com.sofia.oppi.animationengine;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sofia.oppi.Constants;
import com.sofia.oppi.dbUtils.StringXtractor;

import java.util.ArrayList;

/**
 * Created by juanflorez on 04/05/15.
 * Given a file path, get the content.json and create a full
 * ContentPackage object
 */
public class ModuleGsonParser {

    private static final String TAG = "ModuleGsonParser" ;

    public static ContentPackage getContentPackage(String moduleRoot) throws Exception{

        String contentString ="";
        try {
            contentString = StringXtractor.getStringFromFile(moduleRoot+ Constants.CONTENT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ContentPackage thePackage;
        Gson gson = new Gson();
        thePackage = gson.fromJson(contentString, ContentPackage.class);

        if(!thePackage.chapters.isEmpty()) {
           //Get a list with the files for the chapters
           ArrayList<ContentPackage.ChapterName> chaptersFileList = thePackage.chapters;
           for(int i = 0; i< chaptersFileList.size(); i++)
           {
               String chapterPath = moduleRoot+chaptersFileList.get(i).getFileName();
               ContentChapter contentChapter = assembleChapter(chapterPath,moduleRoot);
               thePackage.addChapter(contentChapter);

           }

        }
       // Uncommet: If want to see the final product of the parsing.
       // Log.i(TAG,thePackage.toString());
       // Log.d(TAG,thePackage.getChapter(0).getContentSceneAt(0).getFrames()
       //         .get(0).getImages().get(0).getBitmapName() );
        thePackage.setRoot(moduleRoot);
        return thePackage;

    }

    /**
     * Creates a chapter object with Scnes and images following
     * the instructions in the chapterPath
     * @param chapterPath full file uri for the chapter.json
     * @return
     * @throws Exception if the file is not available
     */
    private static ContentChapter assembleChapter(String chapterPath, String moduleRoot) throws Exception{

        String chapterJsonString =
                StringXtractor.getStringFromFile(chapterPath);
        Gson gson = new Gson();
        ContentChapter contentChapter = gson.fromJson(chapterJsonString,ContentChapter.class);


        // parse the frames for each  scene file in the contentChapter
        ArrayList<ContentScene> sceneArray = contentChapter.getAllContentScenes();
        if(sceneArray != null)
        {
               for (int j = 0; j< sceneArray.size(); j++)
               {
                   String ScenesJsonString =
                           StringXtractor.getStringFromFile(moduleRoot +
                                           sceneArray.get(j).getJsonFile()
                           );
                   Log.d(TAG, "parsing "+sceneArray.get(j).getJsonFile() );
                   ArrayList<Frame> tmpFramesArray = gson.fromJson(ScenesJsonString,
                           new TypeToken<ArrayList<Frame>>(){}
                                   .getType());

                   Log.d(TAG, "Collection size  " + tmpFramesArray.size());
                   ((ContentScene)contentChapter.getSceneAt(j)).setFrames(tmpFramesArray);

               }
        }

        contentChapter.setRoot(moduleRoot);

        ArrayList<ActionScene> actionScenes = new ArrayList<ActionScene>();
        actionScenes = contentChapter.getActionScenes();
        if(actionScenes != null)
        {
            for (ActionScene scene : contentChapter.getActionScenes())
            {
                Log.d(TAG,"Starting action scene: "+ scene.getJsonFile());
                scene.start();
            }
        }
        return contentChapter;
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
