package pelicula.shiri.twostrings.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.adapter.GenreAdapter;
import pelicula.shiri.twostrings.model.TdObject;
import pelicula.shiri.twostrings.utilities.CommonMethods;

public class ExploreFragment extends Fragment {
    ProgressBar mProgressExplore;
    RecyclerView mRecyclerGenre;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_activity_explore);
        } catch(NullPointerException e) {
            Log.e("ExploreFragment", e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewExplore = inflater.inflate(R.layout.fragment_recycler, container, false);

        mProgressExplore = (ProgressBar) viewExplore.findViewById(R.id.pbCommon);
        mProgressExplore.setVisibility(View.INVISIBLE);

        mRecyclerGenre = (RecyclerView) viewExplore.findViewById(R.id.recyclerCommon);
        mRecyclerGenre.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerGenre.setAdapter(new GenreAdapter(CommonMethods.getGenreData(), getContext(), 0));

        return viewExplore;
    }
}
