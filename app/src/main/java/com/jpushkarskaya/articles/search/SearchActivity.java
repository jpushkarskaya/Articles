package com.jpushkarskaya.articles.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jpushkarskaya.articles.utils.ItemClickSupport;
import com.jpushkarskaya.articles.R;
import com.jpushkarskaya.articles.article.Article;
import com.jpushkarskaya.articles.article.ArticleActivity;
import com.jpushkarskaya.articles.article.ArticleAdapter;
import com.jpushkarskaya.articles.filter.Filter;
import com.jpushkarskaya.articles.filter.FilterDialogFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener {

    EditText etQuery;
    Button btnSearch;
    RecyclerView rvResults;

    Filter filter;
    ArrayList<Article> articles;
    ArticleAdapter adapter;

    private EndlessRecyclerViewScrollListener scrollListener;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = 0;
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        articles = new ArrayList<>();
        setUpViews();
    }

    public void setUpViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        rvResults = (RecyclerView) findViewById(R.id.rvResults);
        adapter = new ArticleAdapter(this, articles);
        rvResults.setAdapter(adapter);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(manager);

        // add on click listener
        ItemClickSupport.addTo(rvResults).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                    Article article = articles.get(position);
                    intent.putExtra("article", Parcels.wrap(article));
                    startActivity(intent);
                }
        );

        // endless scrolling
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                doSearch();
            }
        };

        // Adds the scroll listener to RecyclerView
        rvResults.addOnScrollListener(scrollListener);

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
        if (id == R.id.action_filter) {
            showFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(View view) {
        articles.removeAll(articles);
        adapter.notifyDataSetChanged();
        page = 0;
        scrollListener.resetState();
        doSearch();
    }

    private void doSearch() {
        String query = etQuery.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key", "1e412eeff01c467e9ac73a72178bc94d");
        params.put("page", page++);
        params.put("q", query);

        params = Filter.applyFilter(filter, params);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJSONResults = null;

                try {
                    articleJSONResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJSONResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void showFilterDialog() {
        FragmentManager manager = getSupportFragmentManager();
        FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance(filter);
        filterDialogFragment.setFilterDialogListener(this);
        filterDialogFragment.show(manager, "fragment_filter");
    }

    @Override
    public void onFilter(Filter filter) {
        this.filter = filter;
    }
}
