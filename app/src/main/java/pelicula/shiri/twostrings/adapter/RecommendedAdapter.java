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
import pelicula.shiri.twostrings.model.RecommendedObject;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class RecommendedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<RecommendedObject> mDataSet;
    private ImageLoader mImgLoader;
    private Context mContext;

    public RecommendedAdapter(ArrayList<RecommendedObject> data, ImageLoader loader, Context context) {
        mDataSet = data;
        mImgLoader = loader;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View movie = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_recommended, parent, false);
        return new ViewHolderRecommended(movie);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderRecommended holderMovie = (ViewHolderRecommended) holder;
        final RecommendedObject object = mDataSet.get(position);

        String url = TMAUrl.IMAGE_MED_URL + object.getmPoster();
        holderMovie.imgPoster.setImageUrl(url, mImgLoader);
        holderMovie.textTitle.setText(object.getmTitle());

        holderMovie.itemView.setOnClickListener(new View.OnClickListener() {
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
        return mDataSet.size();
    }

    private class ViewHolderRecommended extends RecyclerView.ViewHolder {
        NetworkImageView imgPoster;
        TextView textTitle;

        ViewHolderRecommended(View itemView) {
            super(itemView);
            imgPoster = (NetworkImageView) itemView.findViewById(R.id.imageMovieRecommendedPoster);
            textTitle = (TextView) itemView.findViewById(R.id.textMovieRecommendedTitle);
        }
    }
}
