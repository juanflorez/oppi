package com.sofia.oppi.store;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import com.sofia.oppi.Constants;
import com.sofia.oppi.R;
import com.sofia.oppi.downloader.AppController;
import com.sofia.oppi.downloader.BrReceiver;
import com.sofia.oppi.downloader.Downloader;
import com.sofia.oppi.store.storeDB.ModuleRecord;
import com.sofia.oppi.store.storeDB.ModuleRecordAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class StoreMainActivity extends ActionBarActivity {

    public final String TAG = "StoreMainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ModulesFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
