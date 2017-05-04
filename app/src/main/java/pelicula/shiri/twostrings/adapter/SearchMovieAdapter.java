package pelicula.shiri.twostrings.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import pelicula.shiri.twostrings.MovieDescriptionActivity;
import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.model.MovieObject;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class SearchMovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MovieObject> mDataSet;
    private ImageLoader mImgLoader;
    private Context mContext;

    public SearchMovieAdapter(ArrayList<MovieObject> data, ImageLoader imageLoader,
                              Context context) {
        mDataSet = data;
        mImgLoader = imageLoader;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_search, parent, false);
        return new ViewHolderSearchMovie(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderSearchMovie holderSearchMovie = (ViewHolderSearchMovie) holder;
        final MovieObject object = mDataSet.get(position);

        String imageUrl = TMAUrl.IMAGE_MED_URL + object.getmPoster();
        holderSearchMovie.mMoviePoster.setImageUrl(imageUrl, mImgLoader);
        holderSearchMovie.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieDescriptionActivity.class);
                intent.putExtra("id", object.getmId());
                intent.putExtra("name", object.getmTitle());
                mContext.startActivity(intent);
            }
        });
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
