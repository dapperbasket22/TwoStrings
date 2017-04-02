package pelicula.shiri.twostrings.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pelicula.shiri.twostrings.ExploreActivity;
import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.model.TdObject;

public class GenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<TdObject> mDataSet;
    private Context mContext;

    public GenreAdapter(ArrayList<TdObject> data, Context context) {
        mDataSet = data;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View genre = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_genre, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private class ViewHolderGenre extends RecyclerView.ViewHolder{
        TextView textGenre;

        ViewHolderGenre(View itemView) {
            super(itemView);
            textGenre = (TextView) itemView.findViewById(R.id.textGenreName);
        }
    }
}
