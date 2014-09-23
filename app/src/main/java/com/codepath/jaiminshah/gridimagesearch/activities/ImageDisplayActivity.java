package com.codepath.jaiminshah.gridimagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.codepath.jaiminshah.gridimagesearch.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDisplayActivity extends Activity {
    private MenuItem mItemShare;
    private ShareActionProvider miShareAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
    }

    public void onImageShare(MenuItem item) {
        ImageView siv = (ImageView) findViewById(R.id.ivImageResult);
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(siv);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
//            miShareAction.setShareIntent(shareIntent);
            startActivity(Intent.createChooser(shareIntent, "Share image using"));
        } else {
           Toast.makeText(this,"Sorry something went wrong while sharing",Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_display, menu);

        mItemShare = menu.findItem(R.id.menu_item_share);
        String url = getIntent().getStringExtra("url");
        ImageView ivImageResult = (ImageView) findViewById(R.id.ivImageResult);
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.pbImageDisplay);
        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(url)
                .into(ivImageResult, new Callback() {
                    @Override
                    public void onSuccess() {
                        //      onImageShare();
                        mItemShare.setVisible(true);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                    }
                });
        // Fetch reference to the share action provider
//        miShareAction = (ShareActionProvider) mItemShare.getActionProvider();
        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_share:
                onImageShare(item);
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
