package pelicula.shiri.twostrings.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import pelicula.shiri.twostrings.MovieDescriptionActivity;
import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.model.UpMovieObject;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class UpMovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<UpMovieObject> mDataset;
    private Context mContext;
    private ImageLoader mImgLoader;

    public UpMovieAdapter(ArrayList<UpMovieObject> data, Context context, ImageLoader loader) {
        mDataset = data;
        mContext = context;
        mImgLoader = loader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_upcoming, parent, false);
        return new ViewHolderUpMovie(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderUpMovie upHolder = (ViewHolderUpMovie) holder;
        final UpMovieObject object = mDataset.get(position);

        String backdropUrl = TMAUrl.IMAGE_HIGH_URL + object.getmBackdrop();
        upHolder.movieBackdrop.setImageUrl(backdropUrl, mImgLoader);

        upHolder.movieTitle.setText(object.getmTitle());
        upHolder.movieGenre.setText(object.getmGenre());
        upHolder.movieRelease.setText(object.getmRelease());

        upHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieDescriptionActivity.class);
                intent.putExtra("id", object.getmId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private class ViewHolderUpMovie extends RecyclerView.ViewHolder {
        NetworkImageView movieBackdrop;
        TextView movieTitle, movieGenre, movieRelease;

        ViewHolderUpMovie(View itemView) {
            super(itemView);
            movieBackdrop = (NetworkImageView) itemView.findViewById(R.id.imageUpcomingBackdrop);
            movieTitle = (TextView) itemView.findViewById(R.id.textUpcomingTitle);
            movieGenre = (TextView) itemView.findViewById(R.id.textUpcomingGenre);
            movieRelease = (TextView) itemView.findViewById(R.id.textUpcomingDate);
        }
    }
}
