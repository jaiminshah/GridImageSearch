package com.codepath.jaiminshah.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.jaiminshah.gridimagesearch.R;
import com.codepath.jaiminshah.gridimagesearch.model.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jaimins on 9/19/14.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult>{
    public ImageResultsAdapter(Context context,  ArrayList<ImageResult> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = getItem(position);

        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result,parent,false);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.ivImage.setImageResource(0);
        Picasso.with(getContext())
                .load(imageInfo.tbUrl)
                .into(viewHolder.ivImage);

        viewHolder.tvTitle.setText(Html.fromHtml(imageInfo.title));

        return convertView;
    }

    private static class ViewHolder{
        ImageView ivImage;
        TextView  tvTitle;
    }
}
