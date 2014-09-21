package com.codepath.jaiminshah.gridimagesearch.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.jaiminshah.gridimagesearch.R;
import com.codepath.jaiminshah.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.jaiminshah.gridimagesearch.helpers.EndlessScrollListener;
import com.codepath.jaiminshah.gridimagesearch.model.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends Activity {

    private static final String TAG = SearchActivity.class.getName();

    private EditText metQuery;
    private Button mbtnSearch;
    private GridView mgvResults;
    private ArrayList<ImageResult> mImgResults;
    private ImageResultsAdapter maImgResults;
    private final String mGoogleApi = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&";
    private final int mMaxResults = 64;
    private String mQuery;
    private String mFilters = "";
    private int mNoOfResults = 8;
    private int mPageNo = 0;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        mImgResults = new ArrayList<ImageResult>();
        maImgResults = new ImageResultsAdapter(this, mImgResults);
        mgvResults.setAdapter(maImgResults);
    }

    private void setupViews() {
        metQuery = (EditText) findViewById(R.id.etQuery);
        mbtnSearch = (Button) findViewById(R.id.btnSearch);

        mgvResults = (GridView) findViewById(R.id.gvResults);
        mgvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this,ImageDisplayActivity.class);
                ImageResult imgResult = mImgResults.get(position);
                i.putExtra("url",imgResult.fullUrl);
                startActivity(i);

            }
        });

        mgvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (totalItemsCount > mMaxResults-1){
                    return;
                }
                mPageNo = (page-1)*mNoOfResults;
                fetchQueryData(getSearchUrl());
            }
        });
    }

    public void onImageSearch(View v) {
        mQuery = metQuery.getText().toString();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(metQuery.getWindowToken(), 0);
        startNewSearch();
    }

    private void startNewSearch(){
        mPageNo = 0;
        //Clear the exisiting images from the array.(in case where its a new search)
        maImgResults.clear();
        fetchQueryData(getSearchUrl());
    }

    private void fetchQueryData(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.i("SearchActivity",response.toString());
                JSONArray imageResultsJSON = null;
                try {
                    if (!response.isNull("responseData")) {
                        imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                        //If we found 0 results than show a toast and do nothing.
                        if (imageResultsJSON.length() == 0) {
                            Toast.makeText(getBaseContext(), "Sorry, No results were found :(", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        maImgResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));

                    } else {
                        Toast.makeText(getBaseContext(), "Sorry, we did something wrong :(", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getBaseContext(), "Sorry, Could not connect to Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getSearchUrl(){
        return mGoogleApi +
                "q=" + mQuery +
                "&rsz=" + mNoOfResults +
                "&start=" + mPageNo +
                mFilters;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                invokeSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void invokeSettings(){
        Intent i = new Intent(this,SettingsActivity.class);
        startActivityForResult(i,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            mFilters = data.getExtras().getString("filters");
           startNewSearch();
       }
    }
}
