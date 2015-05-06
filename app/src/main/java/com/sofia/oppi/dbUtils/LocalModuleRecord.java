package com.sofia.oppi.dbUtils;

import android.util.Log;

/**
 * Created by juanflorez on 29/04/15.
 */
public class LocalModuleRecord  {
    public static String TAG ="LocalModuleRecord";
    public String ModuleID;
    public String ModuleName;
    public String IconPath;
    public String root;

    public LocalModuleRecord() {}

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getModuleID() {
        return ModuleID;
    }

    public void setModuleID(String moduleID) {
        ModuleID = moduleID;
    }

    public long getLongID () {
        long result =0;
        try {
            result = Long.parseLong(ModuleID);
        }catch (Exception u){
            Log.d(TAG, "Module " + ModuleID + " Has wrong ID");
        }
        return result;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public void setModuleName(String moduleName) {
        ModuleName = moduleName;
    }

    public String getIconPath() {
        return IconPath;
    }

    public void setIconPath(String iconPath) {
        IconPath = iconPath;
    }
}
