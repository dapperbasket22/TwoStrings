package pelicula.shiri.twostrings.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.SearchActivity;
import pelicula.shiri.twostrings.adapter.UpMovieAdapter;
import pelicula.shiri.twostrings.parser.UpMovieParser;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class HomeFragment extends Fragment {
    RecyclerView mRecyclerUpcoming;
    SearchView mSearchMovie;

    RequestQueue mRequestHome;
    ImageLoader mImgHome;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        } catch(NullPointerException e) {
            Log.e("HomeFragment", e.getMessage());
        }
        mRequestHome = Volley.newRequestQueue(getActivity().getApplicationContext());
        mImgHome = new ImageLoader(mRequestHome, new ImageLoader.ImageCache() {
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
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerUpcoming = (RecyclerView) homeView.findViewById(R.id.recyclerUpcoming);
        mSearchMovie = (SearchView) homeView.findViewById(R.id.searchMovie);

        mRecyclerUpcoming.setLayoutManager(new
                LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(mRecyclerUpcoming);

        mSearchMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals("")) {
                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    intent.putExtra("titleSearch", query);
                    startActivity(intent);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        JsonObjectRequest upRequest = new JsonObjectRequest(Request.Method.GET,
                TMAUrl.UPCOMING_MOVIE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UpMovieParser parseResponse = new UpMovieParser(response);
                mRecyclerUpcoming.setAdapter(new UpMovieAdapter(parseResponse.getmData(),
                        getContext(), mImgHome));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        mRequestHome.add(upRequest);
        return homeView;
    }
}
