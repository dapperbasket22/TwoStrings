package pelicula.shiri.twostrings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.model.SearchMovieObject;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class SearchMovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SearchMovieObject> mDataSet;
    private ImageLoader mImgLoader;

    public SearchMovieAdapter(ArrayList<SearchMovieObject> data, ImageLoader imageLoader) {
        mDataSet = data;
        mImgLoader = imageLoader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_search, parent, false);
        return new ViewHolderSearchMovie(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderSearchMovie holderGenMovie = (ViewHolderSearchMovie) holder;
        final SearchMovieObject object = mDataSet.get(position);

        String imageUrl = TMAUrl.IMAGE_MED_URL + object.getmPoster();
        holderGenMovie.mMoviePoster.setImageUrl(imageUrl, mImgLoader);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private class ViewHolderSearchMovie extends RecyclerView.ViewHolder {
        NetworkImageView mMoviePoster;

        ViewHolderSearchMovie(View v) {
            super(v);
            mMoviePoster = (NetworkImageView) v.findViewById(R.id.imageSearchMoviePoster);
        }
    }
}
