package com.sofia.oppi.store.storeDB;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.sofia.oppi.R;
import com.sofia.oppi.downloader.AppController;
import com.sofia.oppi.downloader.LruBitmapCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanflorez on 23/04/15.
 * this is the data controller for the store.
 * it uses Volley SetImageUrl to dynamically download and display the icons for the module.
 */
public class ModuleRecordAdapter extends ArrayAdapter<ModuleRecord> {

    public static String TAG ="ModuleAdapter";

    private ImageLoader mImageLoader;


    public ModuleRecordAdapter(Context context){
        super(context, R.layout.store_element);

        mImageLoader = new ImageLoader(AppController.getInstance().getRequestQueue(),
                         new LruBitmapCache());
    }

    public void swapRecords(ArrayList<ModuleRecord> objects) {
        clear();

        for(ModuleRecord object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            grid = new View(getContext());
            grid = inflater.inflate(R.layout.store_element, parent, false);


            // TODO: Implement the ViewHolder pattern here
            // NOTE: You would normally use the ViewHolder pattern here
            NetworkImageView imageView = (NetworkImageView) grid.findViewById(R.id.grid_image);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);

            ModuleRecord moduleRecord = getItem(position);
            Log.i(TAG, " Showing Module "+ moduleRecord.getTittle());
            imageView.setImageUrl(moduleRecord.getIconUrl(), mImageLoader);
            textView.setText(moduleRecord.getTittle());

        } else {

            grid = (View) convertView;
        }

        return grid;

    }


}
