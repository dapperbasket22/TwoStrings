package pelicula.shiri.twostrings.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;

import pelicula.shiri.twostrings.ExploreActivity;
import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.model.TdObject;

public class GenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<TdObject> mDataSet;
    private Context mContext;
    private int mLayoutType;
    private int mLastPosition = -1;

    public GenreAdapter(ArrayList<TdObject> data, Context context, int layoutType) {
        mDataSet = data;
        mContext = context;
        mLayoutType = layoutType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View genre;
        if (mLayoutType == 0) {
            genre = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item_genre, parent, false);
        } else {
            genre = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item_movie_genre, parent, false);
        }
        return new ViewHolderGenre(genre);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderGenre holderGenre = (ViewHolderGenre) holder;
        final TdObject object = mDataSet.get(position);

        holderGenre.textGenre.setText(object.getmName());
        holderGenre.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ExploreActivity.class);
                intent.putExtra("genre", object);
                mContext.startActivity(intent);
            }
        });

        setAnimation(holderGenre.itemView, position);
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

    private class ViewHolderGenre extends RecyclerView.ViewHolder{
        TextView textGenre;

        ViewHolderGenre(View itemView) {
            super(itemView);
            textGenre = (TextView) itemView.findViewById(R.id.textGenreName);
        }
    }
}
