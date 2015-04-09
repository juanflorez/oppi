package com.sofia.oppi.animationengine;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class that represents the content
 *
 */
public class ContentPackage {


    //private PagerSection mLearnSection=null;
    //private TrySection mTrySection=null;
    ArrayList<PackageItem> mPackageItems =null;
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
    private String mDuration="";

    /*Empty holder*/
    public ContentPackage () {

    }
//TODO the desktop component should also be added here
    public ContentPackage(String name, Long id, String packVersion, String engVersion, String minRes, String maxRes){
        mName=name;
        mPackageID=id;
        mEngineVersion=engVersion;
        mPackageVersion=packVersion;
        mMinResolution=minRes;
        mMaxResolution=maxRes;
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

    public String getDuration() {
        return mDuration;
    }
}
