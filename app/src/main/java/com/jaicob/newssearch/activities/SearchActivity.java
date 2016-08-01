package com.jaicob.newssearch.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jaicob.newssearch.EndlessRecyclerViewScrollListener;
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

    private Button btnResultSettings;
    private RecyclerView rvResults;
    private ArrayList<Article> articles;
    private ArticleArrayAdapter adapter;
    private SearchSetting searchSettings;

    //region View code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchSettings = new SearchSetting();
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
        paginatedArticleFetch(0,true);
    }

    public void setupViews(){
        rvResults = (RecyclerView) findViewById(R.id.rvResults);
        articles = new ArrayList<>();

        adapter = new ArticleArrayAdapter(this, articles);
        adapter.setOnItemClickListener(new ArticleArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = articles.get(position).getHeadline();
                onArticleItemSelect(view,position);
            }
        });

        rvResults.setAdapter(adapter);
        rvResults.setItemAnimator(new SlideInUpAnimator());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        rvResults.setLayoutManager(gridLayoutManager);
        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                paginatedArticleFetch(page, false);
            }
        });

    }
    //endregion

    //region Menu setup
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setBackgroundColor(Color.WHITE);
        et.setTextColor(Color.DKGRAY);
        et.setHintTextColor(Color.GRAY);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                searchSettings.setQuery(q);
                paginatedArticleFetch(0, true);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region Networking
    public void paginatedArticleFetch(int page, final boolean newSearch) {
        String sort = searchSettings.getSort();
        String beginDate = searchSettings.getBeginDate();
        String fq = searchSettings.getFq();
        String query = searchSettings.getQuery();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key","dfa5b8e84e3e4a8cae57efd2e62310fb");
        params.put("page",page);
        if (!query.isEmpty()) params.put("q",query);
        if (!sort.isEmpty()) params.put("sort",sort);
        if (!fq.isEmpty()) params.put("fq",fq);
        if (!beginDate.isEmpty()) params.put("begin_date", beginDate);

        switch (searchSettings.getArticleLength()){
            case SHORT:
                params.put("fq","word_count:[* TO 400] ");
                break;
            case MEDIUM:
                params.put("fq","word_count:[400 TO 2000] ");
                break;
            case LONG:
                params.put("fq","word_count:[2000 TO *] ");
                break;
        }

        client.get(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray jsonResults = null;
                try {
                    jsonResults = response.getJSONObject("response").getJSONArray("docs");
                    if (newSearch) articles.clear();
                    articles.addAll( Article.fromJsonArray(jsonResults));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d("DEBUG", e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
    //endregion

    //region Event Handlers
    // Handler for clicking the settings button
    public void onResultSettings(View view){
        Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
        settingsIntent.putExtra("searchSettings", Parcels.wrap(searchSettings));
        startActivityForResult(settingsIntent, SEARCH_SETTINGS_REQUEST);
    }


    // Handler for settings result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_SETTINGS_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.d("DEBUG", "success");
                searchSettings = (SearchSetting) Parcels.unwrap(data.getParcelableExtra("searchSettings"));
                paginatedArticleFetch(0, true);
            }
        }
    }

    // Handleer for selecting an item
    public void onArticleItemSelect(View view, int position){
        Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
        Article article = articles.get(position);
        i.putExtra("url",article.getWebUrl());
        startActivity(i);
    }
    //endregion
}
