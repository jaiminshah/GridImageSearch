package com.codepath.jaiminshah.gridimagesearch.model;

/**
 * Created by jaimins on 9/20/14.
 */
public class SearchFilter {
    public String imgSize;
    public String imgColor;
    public String imgType;
    public String siteFilter;

    public SearchFilter(){
        imgSize = "any";
        imgColor = "any";
        imgType = "any";
        siteFilter = "";

    }


    public String getFilterUrl(){
        StringBuilder filterUrl = new StringBuilder("");

        if (!imgSize.equals("any")){
            filterUrl.append("&imgsz="+imgSize);
        }
        if (!imgColor.equals("any")){
            filterUrl.append("&imgcolor="+imgColor);
        }
        if (!imgType.equals("any")){
            filterUrl.append("&imgtype="+imgType);
        }
        if (!siteFilter.equals("")){
            filterUrl.append("&as_sitesearch="+siteFilter);
        }

        return filterUrl.toString();
    }
    public void setImgSize(int selectedPosition){

    }
}
