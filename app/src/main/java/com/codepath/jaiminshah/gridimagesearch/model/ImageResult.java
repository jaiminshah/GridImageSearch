package com.codepath.jaiminshah.gridimagesearch.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jaimins on 9/19/14.
 */
public class ImageResult {

    private static final String TAG = ImageResult.class.getName();

    public String title;
    public String visibleUrl;
    //Data for full image
    public String fullUrl;
    public int width;
    public int height;
    //Data for thumbnail of the image
    public String tbUrl;
    public int tbWidth;
    public int tbHeight;

    public ImageResult(JSONObject imageResult){
        if (imageResult == null){
            return;
        }
        try {
            this.title = imageResult.getString("title");
            this.fullUrl = imageResult.getString("url");
            this.width = imageResult.getInt("width");
            this.height = imageResult.getInt("height");
            this.tbUrl = imageResult.getString("tbUrl");
            this.tbWidth = imageResult.getInt("tbWidth");
            this.tbHeight = imageResult.getInt("tbHeight");
            this.visibleUrl = imageResult.getString("visibleUrl");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray imageResultsJSON){
        ArrayList<ImageResult> imgResults = new ArrayList<ImageResult>();
        for (int i = 0; i < imageResultsJSON.length(); ++i){
            try {
                ImageResult imageResult = new ImageResult(imageResultsJSON.getJSONObject(i));
                imgResults.add(imageResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return imgResults;
    }
}
