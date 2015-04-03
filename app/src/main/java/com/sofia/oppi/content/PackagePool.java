package com.sofia.oppi.content;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *Interface which allows acquiring Content as JSONObject and the available contents list
 */
public interface PackagePool {

    public JSONObject getContent(String name);
    // TODO: perhaps could only include String (the name of the content?) Or contentDescription items (name/uniqueID, animationengineVersion, description, duration, chargeable/downloaded, bitmap, etc.)
    public JSONArray getAvailableContentsList();

}
