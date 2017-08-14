package pelicula.shiri.twostrings.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import pelicula.shiri.twostrings.MovieDescriptionActivity;
import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.model.MovieObject;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MovieObject> mDataSet;
    private ImageLoader mImgLoader;
    private Context mContext;

    private int mLastPosition = -1;
    private final int VIEW_ITEM = 1;

    public MovieAdapter(ArrayList<MovieObject> data, ImageLoader loader, Context context) {
        mDataSet = data;
        mImgLoader = loader;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position) == null ? 0 : VIEW_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item_movie, parent, false);
            vh = new ViewHolderMovie(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item_progress, parent, false);
            vh = new ViewHolderProgress(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderMovie) {
            final ViewHolderMovie holderMovie = (ViewHolderMovie) holder;
            final MovieObject object = mDataSet.get(position);

            String imageUrl = TMAUrl.IMAGE_MED_URL + object.getmPoster();
            holderMovie.imageMoviePoster.setImageUrl(imageUrl, mImgLoader);
            holderMovie.textTitle.setText(object.getmTitle());
            holderMovie.textGenre.setText(object.getmGenre());
            holderMovie.ratingMovie.setRating(object.getmRating());
            String userCount = "(" + object.getmUserCount() + ")";
            holderMovie.textUser.setText(userCount);
            holderMovie.textOverview.setText(object.getmOverview());

            holderMovie.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MovieDescriptionActivity.class);
                    intent.putExtra("id", object.getmId());
                    intent.putExtra("name", object.getmTitle());
                    mContext.startActivity(intent);
                }
            });

            setAnimation(holder.itemView, position);
        } else {
            ((ViewHolderProgress) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > mLastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            mLastPosition = position;
        }
    }

    private class ViewHolderMovie extends RecyclerView.ViewHolder {
        NetworkImageView imageMoviePoster;
        TextView textTitle, textGenre, textUser, textOverview;
        RatingBar ratingMovie;

        ViewHolderMovie(View itemView) {
            super(itemView);
            imageMoviePoster = (NetworkImageView) itemView.findViewById(R.id.imageMovieRecommendedPoster);
            textTitle = (TextView) itemView.findViewById(R.id.textMovieTitle);
            textGenre = (TextView) itemView.findViewById(R.id.textMovieGenre);
            textUser = (TextView) itemView.findViewById(R.id.textMovieUser);
            textOverview = (TextView) itemView.findViewById(R.id.textMovieOverview);
            ratingMovie = (RatingBar) itemView.findViewById(R.id.ratingMovie);
        }
    }

    private class ViewHolderProgress extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ViewHolderProgress(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pbRecycler);
        }
    }
}
