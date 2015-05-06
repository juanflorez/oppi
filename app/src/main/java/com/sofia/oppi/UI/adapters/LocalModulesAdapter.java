package com.sofia.oppi.UI.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sofia.oppi.R;

import com.sofia.oppi.dbUtils.DbModules;
import com.sofia.oppi.dbUtils.InstalledModulesHelper;
import com.sofia.oppi.dbUtils.LocalModuleRecord;

import java.util.ArrayList;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class LocalModulesAdapter extends ArrayAdapter<LocalModuleRecord> {

    public static String TAG ="LocalModules";


    public LocalModulesAdapter(Context context){
        super(context, R.layout.installed_element);

    }

    public void swapRecords(ArrayList<LocalModuleRecord> result) {
        clear();


        for(LocalModuleRecord object : result) {
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
            grid = inflater.inflate(R.layout.installed_element, parent, false);


            // TODO: Implement the ViewHolder pattern here
            // NOTE: You would normally use the ViewHolder pattern here
            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);

            LocalModuleRecord moduleRecord = getItem(position);
            Log.i(TAG, " Showing Module " + moduleRecord.getModuleName());

            imageView.setImageURI(Uri.parse(moduleRecord.getIconPath()));
            textView.setText(moduleRecord.getModuleName());

        } else {

            grid = (View) convertView;
        }

        return grid;

    }

    public void fetchModules(){

        InstalledModulesHelper dbHelper = new InstalledModulesHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getDisplayableModules(db);
        ArrayList<LocalModuleRecord> arrayList = new ArrayList<>();
        // get column indexes
        int nameColumn = cursor.getColumnIndex(DbModules.InstModule.MODULE_NAME);
        int idColumn   = cursor.getColumnIndex(DbModules.InstModule.MODULE_ID);
        int iconPathColumn = cursor.getColumnIndex(DbModules.InstModule.MEDIUM_ICON);
        int rootColumn = cursor.getColumnIndex(DbModules.InstModule.MODULE_ROOT);
        //TODO Change this hardcoded hack
        if(cursor.getCount()==0){
            LocalModuleRecord emptyRecord = new LocalModuleRecord();
            emptyRecord.setModuleName("NO MODULES YET");

            emptyRecord.setIconPath("/storage/sdcard/Sofia/TestLessonV2/icons/big.png");
        }

        while(cursor.moveToNext()){
            LocalModuleRecord tmp = new LocalModuleRecord();
            tmp.setModuleID(cursor.getString(idColumn));
            tmp.setModuleName(cursor.getString(nameColumn));
            tmp.setIconPath(cursor.getString(iconPathColumn));
            tmp.setRoot(cursor.getString(rootColumn));
            arrayList.add(tmp);

        }

        swapRecords(arrayList);

    }


}
