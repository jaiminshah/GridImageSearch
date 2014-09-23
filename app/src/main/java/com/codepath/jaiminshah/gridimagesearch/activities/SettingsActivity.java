package com.codepath.jaiminshah.gridimagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.jaiminshah.gridimagesearch.R;
import com.codepath.jaiminshah.gridimagesearch.model.SearchFilter;

public class SettingsActivity extends Activity {

    private Spinner spnImageSize;
    private Spinner spnColorFilter;
    private Spinner spnImageType;
    private EditText etSiteFilter;
    String [] imgSizeArray;
    String [] imgTypeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupViews();

    }
    private void setupViews(){
        spnImageSize = (Spinner)findViewById(R.id.spnImageSize);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.image_size_array_name,
                android.R.layout.simple_spinner_item);
        spnImageSize.setAdapter(adapter);

        spnColorFilter = (Spinner)findViewById(R.id.spnColorFilter);
        adapter = ArrayAdapter.createFromResource(
                this,
                R.array.image_color_array,
                android.R.layout.simple_spinner_item);
        spnColorFilter.setAdapter(adapter);

        spnImageType = (Spinner)findViewById(R.id.spnImageType);
        adapter = ArrayAdapter.createFromResource(
                this,
                R.array.image_type_array_name,
                android.R.layout.simple_spinner_item);
        spnImageType.setAdapter(adapter);

        etSiteFilter = (EditText)findViewById(R.id.etSiteFilter);

        imgSizeArray = getResources().getStringArray(R.array.image_size_array_value);
        imgTypeArray = getResources().getStringArray(R.array.image_type_array_value);
    }

    public void onSave(View view){
        SearchFilter filter = new SearchFilter();

        filter.imgSize = imgSizeArray[spnImageSize.getSelectedItemPosition()];
        filter.imgColor = spnColorFilter.getSelectedItem().toString();
        filter.imgType = imgTypeArray[spnImageType.getSelectedItemPosition()];
        filter.siteFilter = etSiteFilter.getText().toString();

        Intent data = new Intent();
        data.putExtra("filters",filter.getFilterUrl());
        setResult(RESULT_OK,data);

        finish();
    }

}
