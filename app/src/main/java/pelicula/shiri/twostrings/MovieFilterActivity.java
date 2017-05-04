package pelicula.shiri.twostrings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import pelicula.shiri.twostrings.adapter.FilterGenreAdapter;
import pelicula.shiri.twostrings.model.TdObject;
import pelicula.shiri.twostrings.utilities.CommonMethods;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class MovieFilterActivity extends AppCompatActivity {
    ArrayList<TdObject> mGenreData = CommonMethods.getGenreData();

    RecyclerView mRecyclerGenre;
    FilterGenreAdapter mGenreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerGenre = (RecyclerView) findViewById(R.id.recyclerFilterGenre);
        mRecyclerGenre.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mGenreAdapter = new FilterGenreAdapter(mGenreData, this);
        mRecyclerGenre.setAdapter(mGenreAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent resultIntent = new Intent();

        if (id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        } else if (id == R.id.action_done) {
            Uri baseUri = Uri.parse(TMAUrl.DIS_MOVIE);
            Uri.Builder url = baseUri.buildUpon();
            String genreWith = "";
            String genreWithout = "";
            ArrayList<Integer> mSelection = mGenreAdapter.getmSelectionType();
            for (int i=0; i<mSelection.size(); i++) {
                if (mSelection.get(i) == 1)
                    genreWith += mGenreData.get(i).getmId() + ",";
                if (mSelection.get(i) == -1)
                    genreWithout += mGenreData.get(i).getmId() + ",";
            }
            if (TextUtils.isEmpty(genreWith) && TextUtils.isEmpty(genreWithout)) {
                setResult(RESULT_CANCELED);
            } else {
                if (!TextUtils.isEmpty(genreWith)) {
                    genreWith = genreWith.substring(0, genreWith.length() - 1);
                    url.appendQueryParameter("with_genres", genreWith);
                }
                if (!TextUtils.isEmpty(genreWithout)) {
                    genreWithout = genreWithout.substring(0, genreWithout.length() - 1);
                    url.appendQueryParameter("without_genres", genreWithout);
                }
                resultIntent.putExtra("url", url.toString());
                setResult(RESULT_OK, resultIntent);
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}