package com.codepath.jaiminshah.gridimagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.jaiminshah.gridimagesearch.R;
import com.codepath.jaiminshah.gridimagesearch.adapters.ImageResultsAdapter;
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
    }

    public void onImageSearch(View v) {
        String query = metQuery.getText().toString();
        fetchQueryData(query);
    }

    private void fetchQueryData(String query) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";
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
                        maImgResults.clear(); //Clear the exisiting images from the array.(in case where its a new search)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
