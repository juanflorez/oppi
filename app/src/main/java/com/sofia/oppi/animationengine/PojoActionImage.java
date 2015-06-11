package com.sofia.oppi.animationengine;

import com.google.gson.annotations.SerializedName;

/**
 * classic POJO, no logic yet,
 * Created by juanflorez on 25/05/15.
 */
public class PojoActionImage extends ModuleElement {
    @SerializedName("id")
    public String mName;

    @SerializedName("start_image")
    public String mStart_image;

    @SerializedName("correct")
    public boolean mCorrect;


}
