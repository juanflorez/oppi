package com.sofia.oppi.downloader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


/**
 * Created by juanflorez on 15/04/15.
 * Copy pasted from
 * http://www.androidhive.info/2014/05/android-working-with-volley-library-1/
 */

public class LruBitmapCache extends LruCache<String, Bitmap> implements
        ImageCache {
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        return cacheSize;

    }

    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}