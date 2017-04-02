package pelicula.shiri.twostrings.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.adapter.MovieAdapter;
import pelicula.shiri.twostrings.model.MovieObject;
import pelicula.shiri.twostrings.parser.MovieParser;
import pelicula.shiri.twostrings.utilities.EndlessRecyclerViewScrollListener;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class PopularFragment extends Fragment {
    RecyclerView mRecyclerPopular;
    LinearLayoutManager mRecyclerPopLayout;
    ProgressBar mProgressPop;
    RecyclerView.Adapter mPopAdapter;

    EndlessRecyclerViewScrollListener mScrollListener;
    ArrayList<MovieObject> mData;
    RequestQueue mRequestPopular;
    ImageLoader mImgPopular;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestPopular = Volley.newRequestQueue(getActivity().getApplicationContext());
        mImgPopular = new ImageLoader(mRequestPopular, new ImageLoader.ImageCache() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewPopular = inflater.inflate(R.layout.fragment_recycler, container, false);

        mProgressPop = (ProgressBar) viewPopular.findViewById(R.id.pbCommon);
        mRecyclerPopular = (RecyclerView) viewPopular.findViewById(R.id.recyclerCommon);
        mRecyclerPopLayout = new LinearLayoutManager(getContext());
        mRecyclerPopular.setLayoutManager(mRecyclerPopLayout);

        mData = new ArrayList<>();
        mPopAdapter = new MovieAdapter(mData, mImgPopular, getContext());

        JsonObjectRequest upRequest = new JsonObjectRequest(Request.Method.GET,
                TMAUrl.POPULAR_MOVIE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mProgressPop.setVisibility(View.INVISIBLE);
                MovieParser parseResponse = new MovieParser(response);
                mData = parseResponse.getmData();
                mPopAdapter = new MovieAdapter(mData, mImgPopular, getContext());
                mRecyclerPopular.setAdapter(mPopAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        mRequestPopular.add(upRequest);

        mScrollListener = new EndlessRecyclerViewScrollListener(mRecyclerPopLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page, view, TMAUrl.POPULAR_MOVIE_URL);
            }
        };

        mRecyclerPopular.addOnScrollListener(mScrollListener);

        return viewPopular;
    }

    void loadNextDataFromApi(int page, final RecyclerView view, String urlGen){
        Uri.Builder url = Uri.parse(urlGen).buildUpon();
        url.appendQueryParameter("page",String.valueOf(page));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mProgressPop.setVisibility(View.INVISIBLE);
                updateData(new MovieParser(response).getmData(), view);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressPop.setVisibility(View.INVISIBLE);
            }
        });

        mRequestPopular.add(jsonObjectRequest);
    }

    void updateData(ArrayList<MovieObject> data, RecyclerView view){
        final int size = mPopAdapter.getItemCount();
        mData.addAll(data);

        view.post(new Runnable() {
            @Override
            public void run() {
                mPopAdapter.notifyItemRangeInserted(size, mData.size() - 1);
            }
        });
    }
}
