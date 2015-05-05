package com.sofia.oppi.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.sofia.oppi.downloader.Downloader;
import com.sofia.oppi.store.storeDB.ModuleRecord;
import com.sofia.oppi.store.storeDB.ModuleRecordAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Modules available at the store.
 */
public  class StoreModulesFragment extends Fragment {

    public final String TAG = "StoreModulesFragment";
    private ModuleRecordAdapter modulesAdapter;
    private Downloader downloader;

    public StoreModulesFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        downloader = new Downloader(getActivity());
        modulesAdapter = new ModuleRecordAdapter(getActivity());

        GridView gridView = (GridView) getView().findViewById(R.id.modules_grid);
        gridView.setAdapter(modulesAdapter);

        fetchModuleList();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " +
                        modulesAdapter.getItem(position).getTittle(), Toast.LENGTH_SHORT).show();

                downloader.downloadModule(modulesAdapter.getItem(position).getDownloadURL());
                }

            });
        // Quickly tests offline the view
        // testModuleList();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_store_grid, container, false);
        return rootView;
    }

    /** feeds the grid with a hardcoded list of modules
     *  FOR TEST ONLY !!!
     *  TODO: DELETE ONCE THINGS ARE LOOKING OK
     *
     */
    private  void testModuleList(){

        ArrayList<ModuleRecord> records = new ArrayList<ModuleRecord>();
        for(int i=0; i<4; i++){
            ModuleRecord mod = new ModuleRecord();
            mod.setTittle("test "+ i);
            mod.setIconUrl(
                    "https://www.dropbox.com/s/t9rdwe844riqszs/medium.png?dl=1");
            records.add(mod);
        }
        modulesAdapter.swapRecords(records);


    }
//TODO Refactor this, I do not like to have it here,
// but the reason is that I need access to the modulesAdapter to trigger the swapRecords
    private void fetchModuleList() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.PUBLIC_MODULES, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());
                try {
                    ArrayList<ModuleRecord> modules = parseJson(response.getJSONArray("packages"));
                    modulesAdapter.swapRecords(modules);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }


            }

            //TODO where should this method be?
            private ArrayList<ModuleRecord> parseJson(JSONArray packageList) throws  JSONException {

                Log.d(TAG, "PACKAGE LIST: "+ packageList.toString());
                ArrayList<ModuleRecord> list = new ArrayList<ModuleRecord>();

                for (int x = 0; x < packageList.length(); x++) {

                    ModuleRecord record = new ModuleRecord();
                    JSONObject module = (JSONObject)packageList.get(x);
                    Log.d(TAG, "PACKAGE : "+ x + module.toString());
                    record.setModuleID(module.getString("packageID"));
                    record.setTittle(module.getString("Name"));
                    //TODO add duration and other relevant data to the record displayed in the store
                    String Duration = module.getString("Duration");
                    record.setDownloadURL(module.getString("url"));
                    //TODO dynamically select the best icon for the device
                    record.setIconUrl(module.getString("MediumIcon"));
                    list.add(record);

                }

                return list;
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    private void downloadModule(String Url){

    }
}