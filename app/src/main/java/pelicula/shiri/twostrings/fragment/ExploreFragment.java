package pelicula.shiri.twostrings.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.adapter.GenreAdapter;
import pelicula.shiri.twostrings.model.TdObject;

public class ExploreFragment extends Fragment {
    ProgressBar mProgressExplore;
    RecyclerView mRecyclerGenre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewExplore = inflater.inflate(R.layout.fragment_recycler, container, false);

        mProgressExplore = (ProgressBar) viewExplore.findViewById(R.id.pbCommon);
        mProgressExplore.setVisibility(View.INVISIBLE);

        mRecyclerGenre = (RecyclerView) viewExplore.findViewById(R.id.recyclerCommon);
        mRecyclerGenre.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerGenre.setAdapter(new GenreAdapter(getGenreData(), getContext()));

        return viewExplore;
    }

    private ArrayList<TdObject> getGenreData() {
        ArrayList<TdObject> data = new ArrayList<>();

        data.add(new TdObject(28, "Action"));
        data.add(new TdObject(12, "Adventure"));
        data.add(new TdObject(16, "Animation"));
        data.add(new TdObject(35, "Comedy"));
        data.add(new TdObject(80, "Crime"));
        data.add(new TdObject(99, "Documentary"));
        data.add(new TdObject(18, "Drama"));
        data.add(new TdObject(10751, "Family"));
        data.add(new TdObject(14, "Fantasy"));
        data.add(new TdObject(36, "History"));
        data.add(new TdObject(27, "Horror"));
        data.add(new TdObject(10402, "Music"));
        data.add(new TdObject(9648, "Mystery"));
        data.add(new TdObject(10749, "Romance"));
        data.add(new TdObject(878, "Fiction"));
        data.add(new TdObject(53, "Thriller"));
        data.add(new TdObject(10752, "War"));
        data.add(new TdObject(37, "Western"));

        return data;
    }
}
