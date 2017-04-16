package pelicula.shiri.twostrings.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    private ArrayList<MovieObject> mData;
    private RequestQueue mRequestPopular;
    private ImageLoader mImgPopular;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Popular movies");
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

        mRecyclerPopular.addOnScrollListener(
                new EndlessRecyclerViewScrollListener(mRecyclerPopLayout) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        if (mData.get(mData.size() - 1) != null) {
                            mData.add(null);
                            mPopAdapter.notifyItemInserted(mData.size() - 1);
                        }
                        loadNextDataFromApi(page, TMAUrl.POPULAR_MOVIE_URL);
                    }
                }
        );

        return viewPopular;
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
            }
        });

        mRequestPopular.add(jsonObjectRequest);
    }

    void updateData(ArrayList<MovieObject> data){
        mData.remove(mData.size()-1);
        mData.addAll(data);
        try {
            mPopAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
        }
    }
}
