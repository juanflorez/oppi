package com.sofia.oppi.templates;

import android.util.Log;
import android.view.View;

import com.sofia.oppi.animationengine.ActionScene;

/**
 * Created by juanflorez on 25/05/15.
 */
public  class TemplateFactory {

    private static final String TAG = "Tmpl_Factory";

    public static Template buildTemplate(int templateId, ActionScene scene){
        Template tmp = null;
        switch (templateId) {
            case 1:
                Log.d(TAG, "Building template 1");
                tmp = new TemplateOne(3, 4, scene);
                break;
        }
        return tmp;

    }
}
