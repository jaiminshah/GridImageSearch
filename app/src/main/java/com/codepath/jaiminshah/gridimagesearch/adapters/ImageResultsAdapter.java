package com.codepath.jaiminshah.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.jaiminshah.gridimagesearch.R;
import com.codepath.jaiminshah.gridimagesearch.model.ImageResult;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jaimins on 9/19/14.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult>{

    private static final String TAG = "ImageResultsAdapter";
    private final Random mRandom;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public ImageResultsAdapter(Context context,  ArrayList<ImageResult> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.mRandom = new Random();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = getItem(position);

        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result,parent,false);
            viewHolder.ivImage = (DynamicHeightImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.tvTitle = (DynamicHeightTextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.ivImage.setImageResource(0);

        double positionHeight = getPositionRatio(position);
        viewHolder.ivImage.setHeightRatio(positionHeight);

        Picasso.with(getContext())
                .load(imageInfo.tbUrl)
                .into(viewHolder.ivImage);

        viewHolder.tvTitle.setText(Html.fromHtml(imageInfo.title));

        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
//            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
        // the width
    }

    private static class ViewHolder{
        DynamicHeightImageView ivImage;
        DynamicHeightTextView tvTitle;
    }
}
