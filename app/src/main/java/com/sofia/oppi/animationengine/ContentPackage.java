package com.sofia.oppi.animationengine;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import static java.lang.Integer.*;

/**
 {
 "PackageData": {
 "PackageID": 12150321,
 "Name": "Test V12 new tree Path",
 "PackageVersion": 1,
 "EngineVersion": 6,
 "MinimumResolution": "ldpi",
 "MaximumResolution": "xxxhdpi"
 },
 "Desktop": {
 "SmallIcon": "icons/small.png",
 "MediumIcon": "icons/medium.png",
 "BigIcon": "icons/big.png",
 "Duration": "00:00:37"
 },

 "Chapters" : [
 {"FileName" : "chapters/chapter1.json"},
 {"FileName" : "chapters/chapter2.json"}

 ],

 "Interactive" : [

 {"FileName" : "interactive/chapter3.json"}

 ]
 }
 */
public class ContentPackage extends ModuleElement{

    @SerializedName("PackageData")
    private PackageData packageData;

    @SerializedName("Desktop")
    private Desktop desktop;

    @SerializedName("Chapters")
    ArrayList<ChapterName> chapters = null;


    @SerializedName("Interactive")
    ArrayList<ChapterName> interactiveChapterNames = null;



    ArrayList<ContentChapter> chaptersObj;
    ArrayList<InteractiveChapter> actionChaptersObj;



    private String Name ="";
    private Long   PackageID;
    private String EngineVersion ="";
    private String PackageVersion ="";
    private String MinResolution ="";
    private String MaxResolution ="";





    private int mDuration=0;

    /*Empty holder*/
    public ContentPackage () {

        chaptersObj = new ArrayList<ContentChapter>();
        actionChaptersObj = new ArrayList<InteractiveChapter>();
    }
//TODO the desktop component should also be added here
    public ContentPackage(String name, Long id, String packVersion, String engVersion, String minRes, String maxRes ){
        Name =name;
        PackageID =id;
        EngineVersion =engVersion;
        PackageVersion =packVersion;
        MinResolution =minRes;
        MaxResolution =maxRes;
        chaptersObj = new ArrayList<ContentChapter>();
    }


    public String getName() {
        return packageData.getName();
    }

    public Long getPackageID() {
        return packageData.getPackageID();
    }

    public String getEngineVersion() {
        return packageData.getEngineVersion();
    }

    public String getPackageVersion() {
        return packageData.getPackageVersion();
    }

    public String getMinResolution() {
        return packageData.getMinimumResolution();
    }

    public String getMaxResolution() {
        return packageData.getMaximumResolution();
    }

    public String getSmallIcon() {
        return root+desktop.getSmallIcon();
    }

    public String getMediumIcon() {

        return root+desktop.getMediumIcon();
    }

    public String getBigIcon() {

        return root+desktop.getBigIcon();
    }

    //TODO modify this method to a simple setDuration  after GSON is tested
    public void setDuration( String duration ){
        try {
            String[] times = duration.split(":", 3);
            mDuration = parseInt(times[2]) + parseInt(times[1]) * 60 + parseInt(times[0]) * 3600;
            desktop.setDuration(duration);
        }catch (Exception e) {
            Log.e(getClass().getName(), "Wrong duration for package "+ getName());
            mDuration = 0;
        }

    }
    public int getDuration() {

       String[] times = desktop.getDuration().split(":",3);
       return parseInt(times[2]) + parseInt(times[1])*60 + parseInt(times[0])*3600;
    }

    public void addChapter( ContentChapter contentChapter){
        chaptersObj.add(contentChapter);
    }


    // TODO MUST REFACTOR ONCE IT IS TESTED THAT INTERACTIVE SCENES WORK
    public Chapter getChapter( int ind ){
        ContentChapter contentChapter =null;
        if( chaptersObj != null && ind < chaptersObj.size() && ind >= 0){
            contentChapter = chaptersObj.get( ind );
            return contentChapter;
        }
        if (actionChaptersObj != null && ind+chaptersObj.size() < actionChaptersObj.size())
        {
            InteractiveChapter interactiveChapter = actionChaptersObj.get(0);
            return interactiveChapter;
        }
        return contentChapter;
    }



    public void setDesktop(Desktop desktop){
        this.desktop = desktop;
    }



    public Desktop getDesktop() { return desktop;}


    @Override
    public void setRoot(String path)
    {
        root = path;
        for(int i=0; i< chaptersObj.size();i++){
            chaptersObj.get(i).setRoot(path);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(
                 "ContentPackage{" +
                "packageData=" + packageData +
                ", desktop=" + desktop +
                ", root='" + root + '\'' +
                ", Name='" + Name + '\'' +
                ", PackageID=" + PackageID +"'\n'"+
                ", EngineVersion='" + EngineVersion + '\'' +
                ", PackageVersion='" + PackageVersion + '\'' +
                ", MinResolution='" + MinResolution + '\'' +
                ", MaxResolution='" + MaxResolution + '\'' +
                ", mDuration=" + mDuration + '\'' +
                ", CHAPTERS: "+"'\n'"
         );
        for (int i=0; i<chaptersObj.size();i++){
            builder.append(chaptersObj.get(i).toString());
            builder.append("'\n'");
        }
        builder.append(" Action Chapters:" +'\n');

        for (int j=0; j<actionChaptersObj.size(); j++){
            builder.append(actionChaptersObj.get(j).toString());
            builder.append("\n");

        }

        return builder.toString();
    }


    /**
     *
     * @return a String array with the paths of all the images for the package
     */

    public ArrayList<String> getImagesPaths(){
        ArrayList<String> paths = new ArrayList<String>();
        // get the backgrounds and the images of each frame in each scene
        for(int i=0; i<chaptersObj.size();i++){
            ContentChapter tmpContentChapter = chaptersObj.get(i);

            for(int j=0; j< tmpContentChapter.getAllContentScenes().size(); j++){
                ContentScene tmpScene = (ContentScene)tmpContentChapter.getSceneAt(j);
                paths.add(tmpScene.getBitmapName());

                for(int k=0; k<tmpScene.getFrames().size();k++){
                    ArrayList<Frame> tmpFramesList = tmpScene.getFrames();

                    for(int l=0; l<tmpFramesList.size(); l++){
                        Frame tmpFrame = tmpFramesList.get(l);
                        ArrayList<FrameImage> tmpImageArray = tmpFrame.getImages();

                        for(int m=0; m<tmpImageArray.size(); m++){
                            FrameImage tmpImage = tmpImageArray.get(m);
                            String tmpBitmpPath = tmpImage.getBitmapName();
                            paths.add(tmpBitmpPath);
                        }

                    }


                }
            }

            for(int j=0; j< tmpContentChapter.getActionScenes().size(); j++){
                ActionScene tmpScene = (ActionScene)tmpContentChapter.getActionSceneAt(j);
                paths.add(tmpScene.getBitmapName());
                paths.add(tmpScene.getCorrectImage());
                paths.add(tmpScene.getIncorrectImage());
                for(ActionImage image : tmpScene.actionImages){
                    paths.add(image.getStartImage());
                }
            }

        }

        return  paths;
    }


    static class Desktop {
        private String SmallIcon="";
        private String MediumIcon="";
        private String BigIcon="";
        private String Duration="";

        private Desktop() {
        }

        public String getSmallIcon() {
            return SmallIcon;
        }

        public void setSmallIcon(String smallIcon) {
            SmallIcon = smallIcon;
        }

        public String getMediumIcon() {
            return MediumIcon;
        }

        public void setMediumIcon(String mediumIcon) {
            MediumIcon = mediumIcon;
        }

        public String getBigIcon() {
            return BigIcon;
        }

        public void setBigIcon(String bigIcon) {
            BigIcon = bigIcon;
        }

        public String getDuration() {
            return Duration;
        }

        public void setDuration(String duration) {
            Duration = duration;
        }

        @Override
        public String toString() {
            return "Desktop{" +
                    "SmallIcon='" + SmallIcon + '\'' +
                    ", MediumIcon='" + MediumIcon + '\'' +
                    ", BigIcon='" + BigIcon + '\'' +
                    ", Duration='" + Duration + '\'' +
                    '}';
        }
    }

    static class PackageData {

        @SerializedName("PackageID")
        Long packageID;

        @SerializedName("Name")
        String name = "";

        @SerializedName("PackageVersion")
        String packageVersion = "";

        @SerializedName("EngineVersion")
        String EngineVersion ="";
        String MinimumResolution="";
        String MaximumResolution="";

        String rootPath ="";

        public PackageData(){}



        public Long getPackageID() {
            return packageID;
        }

        public void setPackageID(Long packageID) {
            this.packageID = packageID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackageVersion() {
            return packageVersion;
        }

        public void setPackageVersion(String packageVersion) {
            this.packageVersion = packageVersion;
        }

        public String getEngineVersion() {
            return EngineVersion;
        }

        public void setEngineVersion(String engineVersion) {
            EngineVersion = engineVersion;
        }

        public String getMinimumResolution() {
            return MinimumResolution;
        }

        public void setMinimumResolution(String minimumResolution) {
            MinimumResolution = minimumResolution;
        }

        public String getMaximumResolution() {
            return MaximumResolution;
        }

        public void setMaximumResolution(String maximumResolution) {
            MaximumResolution = maximumResolution;
        }

        @Override
        public String toString() {
            return "PackageData{" +
                    "packageID=" + packageID +
                    ", name='" + name + '\'' +
                    ", packageVersion='" + packageVersion + '\'' +
                    ", EngineVersion='" + EngineVersion + '\'' +
                    ", MinimumResolution='" + MinimumResolution + '\'' +
                    ", MaximumResolution='" + MaximumResolution + '\'' +
                    ", rootPath='" + rootPath + '\'' +
                    '}';
        }
    }

    static class ChapterName {

        @SerializedName("FileName")
        String fileName = "";

        public ChapterName(){}

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }



    }
}
