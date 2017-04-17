package pelicula.shiri.twostrings.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.DividerItemDecoration;
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

import pelicula.shiri.twostrings.BuildConfig;
import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.adapter.CastAdapter;
import pelicula.shiri.twostrings.parser.CreditsParser;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class CastFragment extends Fragment {
    private static final String ARG_MOVIE_ID = "section_number";

    private RequestQueue mRequestCast;
    private ImageLoader mImgLoaderCast;

    ProgressBar mProgressCast;
    RecyclerView mRecyclerCast;

    public CastFragment() {
    }

    public static CastFragment newInstance(int id) {
        CastFragment fragment = new CastFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestCast = Volley.newRequestQueue(getActivity().getApplicationContext());
        mImgLoaderCast = new ImageLoader(mRequestCast, new ImageLoader.ImageCache() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        mProgressCast = (ProgressBar) rootView.findViewById(R.id.pbCommon);
        mRecyclerCast = (RecyclerView) rootView.findViewById(R.id.recyclerCommon);
        mRecyclerCast.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerCast.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerCast.addItemDecoration(dividerItemDecoration);

        JsonObjectRequest castRequest = new JsonObjectRequest(Request.Method.GET, getCastUrl(),
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mProgressCast.setVisibility(View.INVISIBLE);
                CreditsParser parser = new CreditsParser(response);
                mRecyclerCast.setAdapter(new CastAdapter(parser.getmCastData(), mImgLoaderCast));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mRequestCast.add(castRequest);
        return rootView;
    }

    private String getCastUrl() {
        int id = getArguments().getInt(ARG_MOVIE_ID);
        Uri baseUri = Uri.parse(TMAUrl.MOVIE_BASE_URL);
        Uri.Builder url = baseUri.buildUpon().
                appendPath(String.valueOf(id)).
                appendPath("credits").
                appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY);
        return url.toString();
    }
}
