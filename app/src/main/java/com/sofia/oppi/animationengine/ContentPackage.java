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
    private String mName="";
    private Long mPackageID;
    private String mEngineVersion="";
    private String mPackageVersion="";
    private String mMinResoluton="";
    private String mMaxResolution="";

    public ContentPackage(String name, Long id, String packVersion, String engVersion, String minRes, String maxRes){
        mName=name;
        mPackageID=id;
        mEngineVersion=engVersion;
        mPackageVersion=packVersion;
        mMinResoluton=minRes;
        mMaxResolution=maxRes;
    }
    public String getName() {
        return mName;
    }

    public String getEngineVersion() {
        return mEngineVersion;
    }

}
