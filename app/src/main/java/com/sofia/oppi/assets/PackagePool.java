package com.sofia.oppi.assets;

import com.sofia.oppi.animationengine.ContentPackage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for managing content in JSONObject format. When new content is received from server, ContentManager deposits the content.
 * When app is closed, it saves all the downloaded contents to the file as JSONObjects. Implements the ContentPool for
 * acquiring content by ID/name or getting the list of downloaded Contents.
 * If ContentManager does not have the acquired Content, it sends request to the server (ie. serverPacakage) to download the Content. -> TODO: contentLoaderObserver
 */
// TODO: when content related audios and bitmaps are downloaded? Those one which are app included (in the resources) are created on app start?
public class PackagePool {
    private static PackagePool instance;
    HashMap<Long, ContentPackage> mContents=null;
    final private String TAG="PACKAGE_MANAGER";

    protected void PackagePool(){
    }

    public static PackagePool getInstance(){
        if( instance == null ){
            instance = new PackagePool();
        }
        return instance;
    }

    public void addContent(ContentPackage content) {
        if( mContents == null ){
            mContents = new HashMap<Long, ContentPackage>();
        }
        mContents.put( content.getPackageID(), content );
    }

    public ContentPackage getContent( long packageID ) {
        ContentPackage content=null;
        content = mContents.get( packageID );
        return content;
    }

    public ArrayList<ContentPackage> getAvailableContentsList() {
        return null;
    }
}
