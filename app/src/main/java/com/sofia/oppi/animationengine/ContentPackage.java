package com.sofia.oppi.animationengine;

import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Integer.*;

/**
 * Class that represents the content
 *
 */
public class ContentPackage {

    ArrayList<Chapter> mChapters = null;
    private String mName="";
    private Long mPackageID;
    private String mEngineVersion="";
    private String mPackageVersion="";
    private String mMinResolution="";
    private String mMaxResolution="";
    private String mSmallIcon="";
    private String mMediumIcon="";
    private String mBigIcon="";
    private int mDuration=0;

    /*Empty holder*/
    public ContentPackage () {

    }
//TODO the desktop component should also be added here
    public ContentPackage(String name, Long id, String packVersion, String engVersion, String minRes, String maxRes ){
        mName=name;
        mPackageID=id;
        mEngineVersion=engVersion;
        mPackageVersion=packVersion;
        mMinResolution=minRes;
        mMaxResolution=maxRes;
        mChapters = new ArrayList<Chapter>();
    }

    public String getName() {
        return mName;
    }

    public Long getPackageID() {
        return mPackageID;
    }

    public String getEngineVersion() {
        return mEngineVersion;
    }

    public String getPackageVersion() {
        return mPackageVersion;
    }

    public String getMinResolution() {
        return mMinResolution;
    }

    public String getMaxResolution() {
        return mMaxResolution;
    }

    public String getSmallIcon() {
        return mSmallIcon;
    }

    public String getMediumIcon() {
        return mMediumIcon;
    }

    public String getBigIcon() {
        return mBigIcon;
    }

    public void setDuration( String duration ){
        // TODO: PARSE THIS FOR EXAMPLE "00:00:14" NOW TAKES ONLY SECONDS...USE REGEx?
        String seconds = duration.substring( (duration.lastIndexOf( ":") + 1));
        mDuration= parseInt(seconds);
    }
    public int getDuration() {
        return mDuration;
    }

    public void addChapter( Chapter chapter ){
        mChapters.add( chapter );
    }

    public Chapter getChapter( int ind ){
        Chapter chapter=null;
        if( mChapters!= null && ind < mChapters.size()){
            chapter = mChapters.get( ind );
        }
        return chapter;
    }
}
