package pelicula.shiri.twostrings.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pelicula.shiri.twostrings.BuildConfig;
import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.adapter.RecommendedAdapter;
import pelicula.shiri.twostrings.parser.RecommendedParser;
import pelicula.shiri.twostrings.utilities.StartOffsetDecoration;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class InfoFragment extends Fragment {
    private static final String ARG_MOVIE_ID = "movie_id";
    private static final String ARG_IMDB_ID = "imdb_id";
    private static final String ARG_MOVIE_PLOT = "movie_plot";
    private static final String ARG_MOVIE_RATING = "movie_rating";
    private static final String ARG_MOVIE_USERS = "movie_users";
    private static final String ARG_MOVIE_RELEASE = "movie_release";
    private static final String ARG_MOVIE_RUNTIME = "movie_runtime";
    private static final String TAG = "InfoFragment";

    private RequestQueue mRequestInfo;
    private ImageLoader mImgLoaderInfo;

    RecyclerView mRecyclerRecommended;
    TextView mTextRating, mTextImdbRating, mTextTomatoRating, mUserCount, mRelease,
            mRuntime, mOverview;

    public InfoFragment() {
    }

    public static InfoFragment newInstance(int id, String imdbId, String plot, String rating,
                                           String users, String release, String runtime) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, id);
        args.putString(ARG_IMDB_ID, imdbId);
        args.putString(ARG_MOVIE_PLOT, plot);
        args.putString(ARG_MOVIE_RATING, rating);
        args.putString(ARG_MOVIE_USERS, users);
        args.putString(ARG_MOVIE_RELEASE, release);
        args.putString(ARG_MOVIE_RUNTIME, runtime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestInfo = Volley.newRequestQueue(getActivity().getApplicationContext());
        mImgLoaderInfo = new ImageLoader(mRequestInfo, new ImageLoader.ImageCache() {
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
        View infoView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        initViews(infoView);
        setViewData(infoView);
        return infoView;
    }

    private void initViews(View view) {
        mTextRating = (TextView) view.findViewById(R.id.textMovieDetailRating);
        mTextImdbRating = (TextView) view.findViewById(R.id.textImdbRating);
        mTextTomatoRating = (TextView) view.findViewById(R.id.textTomatoRating);
        mUserCount = (TextView) view.findViewById(R.id.textMovieDetailUsers);
        mRelease = (TextView) view.findViewById(R.id.textMovieDetailRelease);
        mRuntime = (TextView) view.findViewById(R.id.textMovieDetailRuntime);
        mOverview = (TextView) view.findViewById(R.id.textMovieDetailOverview);
        mRecyclerRecommended = (RecyclerView) view.findViewById(R.id.recyclerMovieDetailRecommended);
        mRecyclerRecommended.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mRecyclerRecommended.setNestedScrollingEnabled(false);
        mRecyclerRecommended.addItemDecoration(new StartOffsetDecoration(32));
    }

    private void setViewData(View view) {
        String users = "(" + getArguments().getString(ARG_MOVIE_USERS) + " Users)";
        String release = "Released On: " + getArguments().getString(ARG_MOVIE_RELEASE);
        String runtime = "Runtime: " + getArguments().getString(ARG_MOVIE_RUNTIME);

        mTextRating.setText(getArguments().getString(ARG_MOVIE_RATING));
        mUserCount.setText(users);
        mRelease.setText(release);
        mRuntime.setText(runtime);
        mOverview.setText(getArguments().getString(ARG_MOVIE_PLOT));

        omdbData();
        recommendationData(view);
    }

    private void omdbData() {
        String omdbUrl = TMAUrl.OMDB_URL + getArguments().getString(ARG_IMDB_ID);
        JsonObjectRequest omdbRequest = new JsonObjectRequest(Request.Method.GET, omdbUrl,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    String imdbRating = String.valueOf(response.getDouble("imdbRating"));
                    if (TextUtils.isEmpty(imdbRating)) mTextImdbRating.setText("-");
                    else mTextImdbRating.setText(imdbRating);
                    JSONArray ratingArray = response.getJSONArray("Ratings");
                    if (ratingArray.length() > 0) {
                        String rTom = "-";
                        for (int r=0; r<ratingArray.length(); r++) {
                            JSONObject ratingObject = ratingArray.getJSONObject(r);
                            if (ratingObject.getString("Source").equals("Rotten Tomatoes")) {
                                rTom = ratingObject.getString("Value");
                                rTom = rTom.substring(0, rTom.length()-1);
                                break;
                            }
                        }
                        mTextTomatoRating.setText(rTom);
                    } else {
                        mTextTomatoRating.setText("-");
                    }
                } catch (JSONException e) {
                    mTextImdbRating.setText("-");
                    mTextTomatoRating.setText("-");
                    Log.e(TAG, e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextImdbRating.setText("-");
                mTextTomatoRating.setText("-");
            }
        });
        mRequestInfo.add(omdbRequest);
    }

    private void recommendationData(final View view) {
        JsonObjectRequest recommendationRequest = new JsonObjectRequest(Request.Method.GET,
                getRecommendationUrl(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                RecommendedParser parser = new RecommendedParser(response);
                mRecyclerRecommended.setAdapter(new RecommendedAdapter(parser.getmData(),
                        mImgLoaderInfo, getContext()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError)
                    Snackbar.make(view, "No network connection", Snackbar.LENGTH_LONG).show();
            }
        });
        mRequestInfo.add(recommendationRequest);
    }

    private String getRecommendationUrl() {
        int id = getArguments().getInt(ARG_MOVIE_ID);
        Uri baseUri = Uri.parse(TMAUrl.MOVIE_BASE_URL);
        Uri.Builder url = baseUri.buildUpon().
                appendPath(String.valueOf(id)).
                appendPath("recommendations").
                appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY);
        return url.toString();
    }
}
