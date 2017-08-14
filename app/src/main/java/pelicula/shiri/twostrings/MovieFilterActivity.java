package pelicula.shiri.twostrings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import pelicula.shiri.twostrings.adapter.FilterGenreAdapter;
import pelicula.shiri.twostrings.model.TdObject;
import pelicula.shiri.twostrings.utilities.CommonMethods;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class MovieFilterActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<TdObject> mGenreData = CommonMethods.getGenreData();

    RecyclerView mRecyclerGenre;
    Spinner mSpinnerRegion;
    Button mButtonPop, mButtonRat, mButtonCount;
    FilterGenreAdapter mGenreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSpinnerRegion = (Spinner) findViewById(R.id.spinnerFilterRegion);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.region_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerRegion.setAdapter(adapter);

        mRecyclerGenre = (RecyclerView) findViewById(R.id.recyclerFilterGenre);
        mRecyclerGenre.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mGenreAdapter = new FilterGenreAdapter(mGenreData, this);
        mRecyclerGenre.setAdapter(mGenreAdapter);

        mButtonPop = (Button) findViewById(R.id.buttonPop);
        mButtonRat = (Button) findViewById(R.id.buttonRat);
        mButtonCount = (Button) findViewById(R.id.buttonCount);

        mButtonPop.setOnClickListener(this);
        mButtonRat.setOnClickListener(this);
        mButtonCount.setOnClickListener(this);
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

            if (!mButtonPop.isEnabled()) url.appendQueryParameter("sort_by", "popularity.desc");
            else if (!mButtonRat.isEnabled()) {
                url.appendQueryParameter("sort_by", "vote_average.desc");
                url.appendQueryParameter("vote_count.gte", "150");
            } else url.appendQueryParameter("sort_by", "vote_count.desc");

            String region = mSpinnerRegion.getSelectedItem().toString();
            if (!region.equals("All"))
                url.appendQueryParameter("region", CommonMethods.getCountryCode(region));

            String genreWith = "";
            String genreWithout = "";
            ArrayList<Integer> mSelection = mGenreAdapter.getmSelectionType();
            for (int i=0; i<mSelection.size(); i++) {
                if (mSelection.get(i) == 1)
                    genreWith += mGenreData.get(i).getmId() + ",";
                if (mSelection.get(i) == -1)
                    genreWithout += mGenreData.get(i).getmId() + ",";
            }
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonPop) {
            mButtonPop.setEnabled(false);
            mButtonRat.setEnabled(true);
            mButtonCount.setEnabled(true);
        } else if (id == R.id.buttonRat) {
            mButtonRat.setEnabled(false);
            mButtonPop.setEnabled(true);
            mButtonCount.setEnabled(true);
        } else if (id == R.id.buttonCount) {
            mButtonCount.setEnabled(false);
            mButtonRat.setEnabled(true);
            mButtonPop.setEnabled(true);
        }
    }
}