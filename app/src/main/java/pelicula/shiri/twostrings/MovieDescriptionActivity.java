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
import android.view.View;
import android.widget.ImageButton;
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

import org.json.JSONObject;

import java.util.ArrayList;

import pelicula.shiri.twostrings.adapter.KeywordAdapter;
import pelicula.shiri.twostrings.adapter.RecommendedAdapter;
import pelicula.shiri.twostrings.model.MovieDetailObject;
import pelicula.shiri.twostrings.model.RecommendedObject;
import pelicula.shiri.twostrings.model.TdObject;
import pelicula.shiri.twostrings.parser.MovieDetailParser;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class MovieDescriptionActivity extends AppCompatActivity {
    RequestQueue mRequestDetail;
    ImageLoader mImgLoaderDetail;
    String mTrailerKey;

    RatingBar mRating;
    FloatingActionButton mFabTrailer;
    ImageButton mImgButtonClose;
    NetworkImageView mPoster, mBackdrop;
    RecyclerView mRecyclerKeyword, mRecyclerRecommended;
    TextView mTitle, mGenre, mUserCount, mRelease, mRuntime, mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_description);
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
                setViewData(parser.getmMovieData(), parser.getmMovieRecommended().getmData(),
                        parser.getmMovieKeyword());
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

    private String getUrl(int id) {
        Uri baseUri = Uri.parse(TMAUrl.MOVIE_DETAIL_URL);
        Uri.Builder url = baseUri.buildUpon().
                appendPath(String.valueOf(id)).
                appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY).
                appendQueryParameter("append_to_response", "videos,recommendations,keywords");
        return url.toString();
    }

    private void initViews() {
        mPoster = (NetworkImageView) findViewById(R.id.imageMovieDetailPoster);
        mBackdrop = (NetworkImageView) findViewById(R.id.imageMovieDetailBackdrop);
        mFabTrailer = (FloatingActionButton) findViewById(R.id.fabTrailer);
        mTitle = (TextView) findViewById(R.id.textMovieDetailTitle);
        mGenre = (TextView) findViewById(R.id.textMovieDetailGenre);
        mRating = (RatingBar) findViewById(R.id.ratingMovieDetail);
        mUserCount = (TextView) findViewById(R.id.textMovieDetailUsers);
        mRelease = (TextView) findViewById(R.id.textMovieDetailRelease);
        mRuntime = (TextView) findViewById(R.id.textMovieDetailRuntime);
        mOverview = (TextView) findViewById(R.id.textMovieDetailOverview);
        mRecyclerKeyword = (RecyclerView) findViewById(R.id.recyclerMovieDetailKeyword);
        mRecyclerRecommended = (RecyclerView) findViewById(R.id.recyclerMovieDetailRecommended);

        mImgButtonClose = (ImageButton) findViewById(R.id.imgButtonCloseDetail);
        mImgButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setViewData(MovieDetailObject movieDetailObject,
                             ArrayList<RecommendedObject> dataRecommended,
                             ArrayList<TdObject> dataKeyword) {
        String posterUrl = TMAUrl.IMAGE_MED_URL + movieDetailObject.getmPoster();
        String backdropUrl = TMAUrl.IMAGE_MED_URL + movieDetailObject.getmBackdrop();
        mTrailerKey = movieDetailObject.getmTrailer();

        mPoster.setImageUrl(posterUrl, mImgLoaderDetail);
        mBackdrop.setImageUrl(backdropUrl, mImgLoaderDetail);
        mRating.setRating(movieDetailObject.getmRating());
        mTitle.setText(movieDetailObject.getmTitle());
        mGenre.setText(movieDetailObject.getmGenre());
        mUserCount.setText(movieDetailObject.getmUserCount());
        mRelease.setText(movieDetailObject.getmRelease());
        mRuntime.setText(movieDetailObject.getmRuntime());
        mOverview.setText(movieDetailObject.getmOverview());

        mRecyclerRecommended.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerRecommended.setNestedScrollingEnabled(false);
        mRecyclerRecommended.setAdapter(new RecommendedAdapter(dataRecommended, mImgLoaderDetail,
                this));

        mRecyclerKeyword.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerKeyword.setNestedScrollingEnabled(false);
        mRecyclerKeyword.setAdapter(new KeywordAdapter(dataKeyword, this));
    }
}
