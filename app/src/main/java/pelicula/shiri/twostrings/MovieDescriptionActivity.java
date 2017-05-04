package pelicula.shiri.twostrings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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

import pelicula.shiri.twostrings.adapter.GenreAdapter;
import pelicula.shiri.twostrings.fragment.CastFragment;
import pelicula.shiri.twostrings.fragment.InfoFragment;
import pelicula.shiri.twostrings.model.MovieDetailObject;
import pelicula.shiri.twostrings.model.TdObject;
import pelicula.shiri.twostrings.parser.MovieDetailParser;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class MovieDescriptionActivity extends AppCompatActivity {
    private ImageLoader mImgLoaderDetail;

    private String mTrailerKey;
    private MovieDetailObject mMovieObject;

    FloatingActionButton mFabTrailer;
    NetworkImageView mBackdrop;
    RecyclerView mRecyclerGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMovieDetail);
        setSupportActionBar(toolbar);

        int mMovieId = getIntent().getIntExtra("id", 0);
        final String movieName = getIntent().getStringExtra("name");

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().setTitle(movieName);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });

        RequestQueue mRequestDetail = Volley.newRequestQueue(getApplicationContext());
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

        final ViewPager viewPager = (ViewPager) findViewById(R.id.containerMovieDetail);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayoutMovieDetail);
        mBackdrop = (NetworkImageView) findViewById(R.id.imageMovieDetailBackdrop);
        mFabTrailer = (FloatingActionButton) findViewById(R.id.fabTrailer);
        mRecyclerGenre = (RecyclerView) findViewById(R.id.recyclerMovieGenre);

        JsonObjectRequest movieRequest = new JsonObjectRequest(Request.Method.GET, getUrl(mMovieId),
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MovieDetailParser parser = new MovieDetailParser(response);
                mMovieObject = parser.getmMovieData();
                viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
                tabLayout.setupWithViewPager(viewPager);
                setViewData(parser.getmGenreArray());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                appendQueryParameter("append_to_response", "videos");
        return url.toString();
    }

    private void setViewData(ArrayList<TdObject> genreObject) {
        try {
            getSupportActionBar().setTitle(mMovieObject.getmTitle());
        } catch(NullPointerException e) {
            Log.e("MovieDetailActivity", e.getMessage());
        }
        mTrailerKey = mMovieObject.getmTrailer();
        mFabTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mTrailerKey))
                    Snackbar.make(v, "No trailer available yet!", Snackbar.LENGTH_SHORT).show();
                else
                    startActivity(new
                            Intent(Intent.ACTION_VIEW, Uri.parse(TMAUrl.YOU_TUBE_URL+mTrailerKey)));
            }
        });
        mFabTrailer.setVisibility(View.VISIBLE);

        String posterUrl = TMAUrl.IMAGE_HIGH_URL + mMovieObject.getmPoster();
        mBackdrop.setImageUrl(posterUrl, mImgLoaderDetail);

        mRecyclerGenre.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerGenre.setAdapter(new GenreAdapter(genreObject, this, 1));
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (mMovieObject != null)
                    return InfoFragment.newInstance(mMovieObject.getmId(),
                            mMovieObject.getmImdbId(), mMovieObject.getmOverview(),
                            String.valueOf(mMovieObject.getmRating()), mMovieObject.getmUserCount(),
                            mMovieObject.getmRelease(), mMovieObject.getmRuntime());
                else return null;
            }
            else return CastFragment.newInstance(mMovieObject.getmId());
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "INFO";
                case 1:
                    return "CAST";
            }
            return null;
        }
    }
}
