package com.sofia.oppi.UI;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.oppi.R;

import com.sofia.oppi.UI.adapters.LocalModulesAdapter;
import com.sofia.oppi.animationengine.ContentPackage;
import com.sofia.oppi.animationengine.ModuleGsonParser;
import com.sofia.oppi.assets.BitmapPool;
import com.sofia.oppi.assets.PackagePool;


public class InstalledModulesFragment extends Fragment {


    public final String TAG = "InstalledModulesFragment";
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private OnFragmentInteractionListener mListener;
    private ProgressDialog mProgressDialog;

    /**
     * The fragment's ListView/GridView.
     */
    private GridView mGridView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private LocalModulesAdapter mAdapter;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InstalledModulesFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new LocalModulesAdapter(getActivity());
        // Set the adapter
        mGridView = (GridView) getView().findViewById(R.id.installed_modules_grid);
        mGridView.setAdapter(mAdapter);
        mAdapter.fetchModules();

        //Set OnItemClickListener so we can be notified on item clicks
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // TODO: add one button here to start the new activity for testing purposes
                // LATER USER SELECTS THE PACKAGE SHE WANTS TO SEE FROM THE LIST.

                long tmpID = mAdapter.getItem(position).getLongID();
                String stringID = Long.toString(tmpID);
                String tmpRoot = mAdapter.getItem(position).getRoot();

                new LoadPackageAsync().execute(stringID,tmpRoot);

                ContentPackage contentPackage = new ContentPackage();
                try{
                   contentPackage = ModuleGsonParser.getContentPackage(tmpRoot);
                   } catch (Exception e) {
                        e.printStackTrace();
                   }
                PackagePool.getInstance().addContent(contentPackage);
                BitmapPool.getInstance().loadImages(getActivity(), contentPackage.getImagesPaths());

            }

        });


    }


    protected Dialog showDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setMessage("Loading module ...");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_installed_grid, container, false);
        // TODO: Change Adapter to display your content

        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(LocalModules.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mGridView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    class LoadPackageAsync extends AsyncTask<String,String,String>{

        Intent intent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... params) {

            // TODO: add one button here to start the new activity for testing purposes
            // LATER USER SELECTS THE PACKAGE SHE WANTS TO SEE FROM THE LIST.
            intent  = new Intent( getActivity(), ContentActivity.class );
            intent.putExtra( "PACKAGE_ID", Long.valueOf(params[0]) );
            intent.putExtra("PACKAGE_ROOT", params[1]);
            ContentPackage contentPackage = new ContentPackage();
            try{
                contentPackage = ModuleGsonParser.getContentPackage(params[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            PackagePool.getInstance().addContent(contentPackage);
            BitmapPool.getInstance().loadImages(getActivity(), contentPackage.getImagesPaths());

            return null;
        }

        @Override
        protected void onPostExecute(String unused) {
          mProgressDialog.setProgress(100);
          mProgressDialog.dismiss();
          startActivity(intent); // calls pause for this activity!
        }
    }

}
