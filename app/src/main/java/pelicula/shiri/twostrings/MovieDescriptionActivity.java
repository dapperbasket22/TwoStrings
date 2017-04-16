package pelicula.shiri.twostrings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pelicula.shiri.twostrings.adapter.GenreAdapter;
import pelicula.shiri.twostrings.adapter.RecommendedAdapter;
import pelicula.shiri.twostrings.model.MovieDetailObject;
import pelicula.shiri.twostrings.model.RecommendedObject;
import pelicula.shiri.twostrings.model.TdObject;
import pelicula.shiri.twostrings.parser.MovieDetailParser;
import pelicula.shiri.twostrings.utilities.StartOffsetDecoration;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class MovieDescriptionActivity extends AppCompatActivity {
    private static final String TAG = "MovieDetail";
    RequestQueue mRequestDetail;
    ImageLoader mImgLoaderDetail;
    String mTrailerKey;

    Toolbar mToolbar;
    RatingBar mRating;
    FloatingActionButton mFabTrailer;
    NetworkImageView mBackdrop;
    RecyclerView mRecyclerGenre, mRecyclerRecommended;
    TextView mTextRating, mTextImdbRating, mTextTomatoRating, mUserCount, mRelease,
            mRuntime, mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_description);
        mToolbar = (Toolbar) findViewById(R.id.toolbarMovieDetail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRequestDetail = Volley.newRequestQueue(getApplicationContext());
        mImgLoaderDetail = new ImageLoader(mRequestDetail, new ImageLoader.ImageCache() {
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

        int mMovieId = getIntent().getIntExtra("id", 0);
        mTrailerKey = "";
        initViews();
        JsonObjectRequest movieRequest = new JsonObjectRequest(Request.Method.GET, getUrl(mMovieId),
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MovieDetailParser parser = new MovieDetailParser(response);
                setViewData(parser.getmMovieData(), parser.getmGenreArray(),
                        parser.getmMovieRecommended().getmData());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mFabTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTrailerKey.equals(""))
                    Snackbar.make(v, "Fetching Data", Snackbar.LENGTH_SHORT).show();
                else if (mTrailerKey.isEmpty())
                    Snackbar.make(v, "No Trailer Available", Snackbar.LENGTH_SHORT).show();
                else
                    startActivity(new
                            Intent(Intent.ACTION_VIEW, Uri.parse(TMAUrl.YOU_TUBE_URL+mTrailerKey)));
            }
        });

        mRequestDetail.add(movieRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getUrl(int id) {
        Uri baseUri = Uri.parse(TMAUrl.MOVIE_DETAIL_URL);
        Uri.Builder url = baseUri.buildUpon().
                appendPath(String.valueOf(id)).
                appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY).
                appendQueryParameter("append_to_response", "videos,recommendations");
        return url.toString();
    }

    private void initViews() {
        mBackdrop = (NetworkImageView) findViewById(R.id.imageMovieDetailBackdrop);
        mFabTrailer = (FloatingActionButton) findViewById(R.id.fabTrailer);
        mTextRating = (TextView) findViewById(R.id.textMovieDetailRating);
        mRating = (RatingBar) findViewById(R.id.ratingMovieDetail);
        mTextImdbRating = (TextView) findViewById(R.id.textImdbRating);
        mTextTomatoRating = (TextView) findViewById(R.id.textTomatoRating);
        mUserCount = (TextView) findViewById(R.id.textMovieDetailUsers);
        mRelease = (TextView) findViewById(R.id.textMovieDetailRelease);
        mRuntime = (TextView) findViewById(R.id.textMovieDetailRuntime);
        mOverview = (TextView) findViewById(R.id.textMovieDetailOverview);
        mRecyclerGenre = (RecyclerView) findViewById(R.id.recyclerMovieGenre);
        mRecyclerRecommended = (RecyclerView) findViewById(R.id.recyclerMovieDetailRecommended);
    }

    private void setViewData(final MovieDetailObject movieDetailObject,
                             ArrayList<TdObject> genreObject,
                             ArrayList<RecommendedObject> dataRecommended) {
        String posterUrl = TMAUrl.IMAGE_HIGH_URL + movieDetailObject.getmPoster();
        getSupportActionBar().setTitle(movieDetailObject.getmTitle());
        mTrailerKey = movieDetailObject.getmTrailer();

        mBackdrop.setImageUrl(posterUrl, mImgLoaderDetail);
        mRating.setRating(movieDetailObject.getmRating()/2);
        mTextRating.setText((Float.toString(movieDetailObject.getmRating())));
        String users = movieDetailObject.getmUserCount() + " Voters";
        mUserCount.setText(users);
        mRelease.setText(movieDetailObject.getmRelease());
        mRuntime.setText(movieDetailObject.getmRuntime());
        mOverview.setText(movieDetailObject.getmOverview());

        mRecyclerGenre.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerGenre.setAdapter(new GenreAdapter(genreObject, this, 1));

        String omdbUrl = TMAUrl.OMDB_URL + movieDetailObject.getmImdbId();
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
                Log.e(TAG, error.getMessage());
            }
        });

        mRequestDetail.add(omdbRequest);

        mRecyclerRecommended.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerRecommended.setNestedScrollingEnabled(false);
        mRecyclerRecommended.setAdapter(new RecommendedAdapter(dataRecommended, mImgLoaderDetail,
                this));
        mRecyclerRecommended.addItemDecoration(new StartOffsetDecoration(32));
    }
}
