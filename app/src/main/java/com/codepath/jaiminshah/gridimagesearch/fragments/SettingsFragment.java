package com.codepath.jaiminshah.gridimagesearch.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.jaiminshah.gridimagesearch.R;
import com.codepath.jaiminshah.gridimagesearch.model.SearchFilter;

/**
 * Created by jaimins on 9/22/14.
 */
public class SettingsFragment extends DialogFragment {

    private Spinner spnImageSize;
    private Spinner spnColorFilter;
    private Spinner spnImageType;
    private EditText etSiteFilter;
    String [] imgSizeArray;
    String [] imgTypeArray;

    public SettingsFragment(){

    }

    public static SettingsFragment newInstance(){
        SettingsFragment settingsFragment = new SettingsFragment();

        return settingsFragment;
    }

    private void setupViews(View view){
        spnImageSize = (Spinner)view.findViewById(R.id.spnImageSize);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.image_size_array_name,
                android.R.layout.simple_spinner_dropdown_item);
        spnImageSize.setAdapter(adapter);

        spnColorFilter = (Spinner)view.findViewById(R.id.spnColorFilter);
        adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.image_color_array,
                android.R.layout.simple_spinner_dropdown_item);
        spnColorFilter.setAdapter(adapter);

        spnImageType = (Spinner)view.findViewById(R.id.spnImageType);
        adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.image_type_array_name,
                android.R.layout.simple_spinner_dropdown_item);
        spnImageType.setAdapter(adapter);

        etSiteFilter = (EditText)view.findViewById(R.id.etSiteFilter);

        imgSizeArray = getResources().getStringArray(R.array.image_size_array_value);
        imgTypeArray = getResources().getStringArray(R.array.image_type_array_value);
    }

    public interface SettingsFragmentListener{
        public void onSaveButtonClick(String filters);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle(R.string.title_activity_settings);
        View view = inflater.inflate(R.layout.activity_settings,null,false);
        setupViews(view);
        builder.setView(view)
                .setPositiveButton(R.string.label_btn_save,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SettingsFragmentListener listener = (SettingsFragmentListener) getActivity();
                        listener.onSaveButtonClick(onSave());
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
        return builder.create();
    }

    public String onSave(){
        SearchFilter filter = new SearchFilter();

        filter.imgSize = imgSizeArray[spnImageSize.getSelectedItemPosition()];
        filter.imgColor = spnColorFilter.getSelectedItem().toString();
        filter.imgType = imgTypeArray[spnImageType.getSelectedItemPosition()];
        filter.siteFilter = etSiteFilter.getText().toString();

        return filter.getFilterUrl();
    }
}
