package pelicula.shiri.twostrings;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONObject;

import java.util.ArrayList;

import pelicula.shiri.twostrings.adapter.SearchMovieAdapter;
import pelicula.shiri.twostrings.model.SearchMovieObject;
import pelicula.shiri.twostrings.parser.SearchMovieParser;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class SearchActivity extends AppCompatActivity {
    private static final String LOG_TAG_SEARCH = "SearchActivity";
    RequestQueue mRequestSearch;
    ImageLoader mImgSearch;

    DiscreteScrollView mScrollSearch;
    TextView mTextTitle, mTextGenre, mTextUser, mTextOverview;
    RatingBar mRatingMovie;
    Button mButtonView;
    ProgressBar mProgressSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String query = getIntent().getStringExtra("titleSearch");
        getSupportActionBar().setTitle(query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRequestSearch = Volley.newRequestQueue(getApplicationContext());
        mImgSearch = new ImageLoader(mRequestSearch, new ImageLoader.ImageCache() {
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

        mScrollSearch = (DiscreteScrollView) findViewById(R.id.scrollViewSearchMovie);
        mTextTitle = (TextView) findViewById(R.id.textSearchMovieTitle);
        mTextGenre = (TextView) findViewById(R.id.textSearchMovieGenre);
        mRatingMovie = (RatingBar) findViewById(R.id.ratingSearchMovie);
        mTextUser = (TextView) findViewById(R.id.textSearchMovieUser);
        mTextOverview = (TextView) findViewById(R.id.textSearchMovieOverview);
        mButtonView = (Button) findViewById(R.id.buttonMovieView);
        mProgressSearch = (ProgressBar) findViewById(R.id.pbSearch);

        mScrollSearch.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .build());

        searchRequest(query);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void searchRequest(String q) {
        JsonObjectRequest requestData = new JsonObjectRequest(Request.Method.GET,
                getUrl(q), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setupContent(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mRequestSearch.add(requestData);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            getSupportActionBar().setTitle(query);
            resetFields();
            searchRequest(query);
        }
    }

    private void resetFields() {
        try {
            mScrollSearch.setAdapter(new SearchMovieAdapter(new ArrayList<SearchMovieObject>(), mImgSearch));
            mTextTitle.setText("");
            mTextGenre.setText("");
            mRatingMovie.setRating(0);
            mTextUser.setText("");
            mTextOverview.setText("");
            mRatingMovie.setVisibility(View.INVISIBLE);
            mTextUser.setVisibility(View.INVISIBLE);
            mButtonView.setVisibility(View.INVISIBLE);
            mProgressSearch.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(LOG_TAG_SEARCH, e.getMessage());
        }
    }

    private String getUrl(String q) {
        Uri baseUri = Uri.parse(TMAUrl.SEARCH_URL);
        Uri.Builder url = baseUri.buildUpon()
                .appendQueryParameter("query", q);
        return url.toString();
    }

    private void setupContent(JSONObject response){
        SearchMovieParser parseResponse = new SearchMovieParser(response);
        final ArrayList<SearchMovieObject> data = parseResponse.getmData();

        mProgressSearch.setVisibility(View.INVISIBLE);
        mScrollSearch.setAdapter(new SearchMovieAdapter(data, mImgSearch));

        try {
            SearchMovieObject firstItem = data.get(0);
            mTextTitle.setText(firstItem.getmTitle());
            mTextGenre.setText(firstItem.getmGenre());
            mRatingMovie.setRating(firstItem.getmRating());
            mTextUser.setText(firstItem.getmUserCount());
            mTextOverview.setText(firstItem.getmOverview());
            mButtonView.setVisibility(View.VISIBLE);
            mRatingMovie.setVisibility(View.VISIBLE);
            mTextUser.setVisibility(View.VISIBLE);
        } catch (Exception e){
            Log.e(LOG_TAG_SEARCH, e.getMessage());
        }

        mScrollSearch.setOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@NonNull RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                SearchMovieObject movieItem = data.get(adapterPosition);
                mTextTitle.setText(movieItem.getmTitle());
                mTextGenre.setText(movieItem.getmGenre());
                mRatingMovie.setRating(movieItem.getmRating());
                mTextUser.setText(movieItem.getmUserCount());
                mTextOverview.setText(movieItem.getmOverview());
            }
        });

        mButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            Intent intent = new Intent(SearchActivity.this, MovieDescriptionActivity.class);
                            SearchMovieObject currentItem = data.get(mScrollSearch.getCurrentItem());
                            intent.putExtra("id", currentItem.getmId());
                            startActivity(intent);
            }
        });
    }
}
