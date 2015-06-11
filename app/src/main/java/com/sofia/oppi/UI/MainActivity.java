package com.sofia.oppi.UI;

import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.sofia.oppi.Constants;
import com.sofia.oppi.R;




import com.sofia.oppi.UI.InstalledModulesFragment.OnFragmentInteractionListener;
import com.sofia.oppi.downloader.BrReceiver;
import com.sofia.oppi.install.Installer;


// TODO: support ONLY LANDSCAPE!!!


public class MainActivity extends ActionBarActivity
                              implements OnFragmentInteractionListener {
    final private String TAG="MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new InstalledModulesFragment())
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
              installTestModule();
              return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void installTestModule() {
        // TODO:  Do RnD  the righ way.
        if (Constants.RnD_OFFLINE_MODE){
            BrReceiver receiver = new BrReceiver();
            // TODO add the unZiped path to the modules database
            Log.i(TAG,"RnD Mode");
            String uZipedLocal =
                    receiver.unZipFile( System.getenv("EXTERNAL_STORAGE"),
                            System.getenv("EXTERNAL_STORAGE")+
                    "/TestLesson.zip");
            Log.i(TAG,uZipedLocal);
            Installer.getInstance(this).registerDirectory(uZipedLocal);
            // How to refresh

        }
    }

    /**
     * to communicate between fragment and activity
     * @param id
     */
    public void onFragmentInteraction(String id){
         //TODO decide if it is needed
    }



}
