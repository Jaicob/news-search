package com.jaicob.newssearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jaicob.newssearch.R;
import com.jaicob.newssearch.adapters.ArticleArrayAdapter;
import com.jaicob.newssearch.models.Article;
import com.jaicob.newssearch.models.SearchSetting;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class SearchActivity extends AppCompatActivity {
    public static final int SEARCH_SETTINGS_REQUEST = 100;

    private EditText etQuery;
    private Button btnSearch;
    private Button btnResultSettings;
    private RecyclerView rvResults;
    private ArrayList<Article> articles;
    private ArticleArrayAdapter adapter;
    private SearchSetting searchSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchSettings = new SearchSetting();
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    public void setupViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        rvResults = (RecyclerView) findViewById(R.id.rvResults);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);

        rvResults.setAdapter(adapter);
        rvResults.setLayoutManager(new GridLayoutManager(this,3));
        rvResults.setItemAnimator(new SlideInUpAnimator());
        adapter.setOnItemClickListener(new ArticleArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = articles.get(position).getHeadline();
                onArticleItemSelect(view,position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_SETTINGS_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.d("DEBUG", "success");
                searchSettings = (SearchSetting) Parcels.unwrap(data.getParcelableExtra("searchSettings"));
            }
        }
    }

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();
        String sort = searchSettings.getSort();
        String beginDate = searchSettings.getBeginDate();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key","dfa5b8e84e3e4a8cae57efd2e62310fb");
        params.put("page",0);
        params.put("q",query);
        params.put("sort",sort);

        System.out.println("DEBUG " + beginDate);
        if (!beginDate.isEmpty()) {
            params.put("begin_date", beginDate);
        }

        client.get(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray jsonResults = null;
                try {
                    jsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.clear();
                    articles.addAll( Article.fromJsonArray(jsonResults));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d("DEBUG", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public void onResultSettings(View view){
        Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
        settingsIntent.putExtra("searchSettings", Parcels.wrap(searchSettings));
        startActivityForResult(settingsIntent, SEARCH_SETTINGS_REQUEST);
    }

    public void onArticleItemSelect(View view, int position){
        Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
        Article article = articles.get(position);
        i.putExtra("url",article.getWebUrl());
        startActivity(i);
    }
}
