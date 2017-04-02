package pelicula.shiri.twostrings;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

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
    RecyclerView mRecyclerExplore;
    LinearLayoutManager mRecyclerExploreLayout;
    ProgressBar mProgressExplore;
    RecyclerView.Adapter mExploreAdapter;

    EndlessRecyclerViewScrollListener mScrollListener;
    ArrayList<MovieObject> mData;
    RequestQueue mRequestExplore;
    ImageLoader mImgExplore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarExplore);
        setSupportActionBar(toolbar);

        TdObject genObject = (TdObject) getIntent().getSerializableExtra("genre");
        final String genMovieUrl = TMAUrl.EXPLORE_MOVIE_URL + genObject.getmId();

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

        JsonObjectRequest upRequest = new JsonObjectRequest(Request.Method.GET,
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
            }
        });

        mRequestExplore.add(upRequest);

        mScrollListener = new EndlessRecyclerViewScrollListener(mRecyclerExploreLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page, view, genMovieUrl);
            }
        };

        mRecyclerExplore.addOnScrollListener(mScrollListener);

    }

    void loadNextDataFromApi(int page, final RecyclerView view, String urlGen){
        Uri.Builder url = Uri.parse(urlGen).buildUpon();
        url.appendQueryParameter("page",String.valueOf(page));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mProgressExplore.setVisibility(View.INVISIBLE);
                updateData(new MovieParser(response).getmData(), view);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressExplore.setVisibility(View.INVISIBLE);
            }
        });

        mRequestExplore.add(jsonObjectRequest);
    }

    void updateData(ArrayList<MovieObject> data, RecyclerView view){
        final int size = mExploreAdapter.getItemCount();
        mData.addAll(data);

        view.post(new Runnable() {
            @Override
            public void run() {
                mExploreAdapter.notifyItemRangeInserted(size, mData.size() - 1);
            }
        });
    }
}