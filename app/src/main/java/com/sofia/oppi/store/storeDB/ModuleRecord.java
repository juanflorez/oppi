package com.sofia.oppi.store.storeDB;

/**
 * Created by juanflorez on 23/04/15.
 * represents the module that will be displayed in the storeview
 */
public class ModuleRecord {
    private String iconUrl;
    private String tittle;
    private String downloadURL;
    private String moduleID;

    public ModuleRecord(){
        this.iconUrl ="";
        this.tittle  ="";
        this.downloadURL ="";
        this.moduleID ="";
    }

    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getModuleID() {
        return moduleID;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getTittle() {
        return tittle;
    }

    public String getDownloadURL() {
        return downloadURL;
    }
}
