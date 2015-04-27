package com.sofia.oppi.downloader;

/**
 * Created by juanflorez on 15/04/15.
 * Copy pasted from http://www.androidhive.info/2014/05/android-working-with-volley-library-1/
 * Added methods to keep installed modules up to date
 *
 *
 * This is a singleton that handles the volley http connections.
 * it is used to everything, from downloading modules to updating the store and
 * getting new icons
 */


import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private List<Long> pendingModules;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        pendingModules = new ArrayList<Long>();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public  ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    public void addPendingModule (Long queue){

        pendingModules.add(queue);
    }

    public List<Long> getPendingModules(){
        return pendingModules;
    }

    public boolean removeFromPendingModules(Long queue){
        return pendingModules.remove(queue);

    }
// Will give the application context, even outside of activities.
    public Context getContext(){

        return getApplicationContext();

    }


}
