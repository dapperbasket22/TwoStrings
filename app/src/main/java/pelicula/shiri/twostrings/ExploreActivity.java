package pelicula.shiri.twostrings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import pelicula.shiri.twostrings.adapter.MovieAdapter;
import pelicula.shiri.twostrings.model.MovieObject;
import pelicula.shiri.twostrings.model.TdObject;
import pelicula.shiri.twostrings.parser.MovieParser;
import pelicula.shiri.twostrings.utilities.EndlessRecyclerViewScrollListener;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class ExploreActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private String genMovieUrl;

    RecyclerView mRecyclerExplore;
    LinearLayoutManager mRecyclerExploreLayout;
    ProgressBar mProgressExplore;
    RecyclerView.Adapter mExploreAdapter;

    private EndlessRecyclerViewScrollListener mScrollListener;
    private ArrayList<MovieObject> mData;
    private RequestQueue mRequestExplore;
    private ImageLoader mImgExplore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarExplore);
        setSupportActionBar(toolbar);

        TdObject genObject = (TdObject) getIntent().getSerializableExtra("genre");
        genMovieUrl = TMAUrl.EXPLORE_MOVIE_URL + genObject.getmId();

        mRequestExplore = Volley.newRequestQueue(getApplicationContext());
        mImgExplore = new ImageLoader(mRequestExplore, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap>
                    cache = new LruCache<>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });

        mProgressExplore = (ProgressBar) findViewById(R.id.pbExplore);
        mRecyclerExplore = (RecyclerView) findViewById(R.id.recyclerExplore);
        mRecyclerExploreLayout = new LinearLayoutManager(this);
        mRecyclerExplore.setLayoutManager(mRecyclerExploreLayout);

        mData = new ArrayList<>();
        mExploreAdapter = new MovieAdapter(mData, mImgExplore, this);

        JsonObjectRequest disRequest = new JsonObjectRequest(Request.Method.GET,
                genMovieUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mProgressExplore.setVisibility(View.INVISIBLE);
                MovieParser parseResponse = new MovieParser(response);
                mData = parseResponse.getmData();
                mExploreAdapter = new MovieAdapter(mData, mImgExplore, ExploreActivity.this);
                mRecyclerExplore.setAdapter(mExploreAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressExplore.setVisibility(View.INVISIBLE);
                errorSnackbar(error);
            }
        });
        mRequestExplore.add(disRequest);

        mScrollListener = new EndlessRecyclerViewScrollListener(mRecyclerExploreLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (mData.get(mData.size() - 1) != null) {
                    mData.add(null);
                    mExploreAdapter.notifyItemInserted(mData.size() - 1);
                }
                loadNextDataFromApi(page, genMovieUrl);
            }
        };
        mRecyclerExplore.addOnScrollListener(mScrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_explore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter) {
            startActivityForResult(new Intent(this, MovieFilterActivity.class), REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                genMovieUrl = data.getStringExtra("url");
                mData.clear();
                mScrollListener.resetState();
                mExploreAdapter.notifyDataSetChanged();
                mProgressExplore.setVisibility(View.VISIBLE);

                JsonObjectRequest disRequest2 = new JsonObjectRequest(Request.Method.GET,
                        genMovieUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mProgressExplore.setVisibility(View.INVISIBLE);
                        MovieParser parseResponse = new MovieParser(response);
                        mData.addAll(parseResponse.getmData());
                        mExploreAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorSnackbar(error);
                    }
                });
                mRequestExplore.add(disRequest2);
            }
        }
    }

    void loadNextDataFromApi(int page, String urlGen){
        Uri.Builder url = Uri.parse(urlGen).buildUpon();
        url.appendQueryParameter("page",String.valueOf(page));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateData(new MovieParser(response).getmData());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorSnackbar(error);
            }
        });

        mRequestExplore.add(jsonObjectRequest);
    }

    void errorSnackbar(VolleyError error) {
        if(error instanceof NetworkError)
            Snackbar.make(findViewById(R.id.exploreCoordLayout),
                    "No network connection", Snackbar.LENGTH_LONG).show();
    }

    void updateData(ArrayList<MovieObject> data){
        mData.remove(mData.size()-1);
        mData.addAll(data);
        try {
            mExploreAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(ExploreActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
        }
    }
}