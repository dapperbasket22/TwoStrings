package pelicula.shiri.twostrings.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.model.TdObject;

public class KeywordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TdObject> mDataSet;
    private Context mContext;

    public KeywordAdapter(ArrayList<TdObject> data, Context context) {
        mDataSet = data;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View genre = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_keywords, parent, false);
        return new ViewHolderKeyword(genre);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderKeyword holderGenre = (ViewHolderKeyword) holder;
        final TdObject object = mDataSet.get(position);

        holderGenre.textGenre.setText(object.getmName());
        holderGenre.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //By Keyword
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private class ViewHolderKeyword extends RecyclerView.ViewHolder{
        TextView textGenre;

        ViewHolderKeyword(View itemView) {
            super(itemView);
            textGenre = (TextView) itemView.findViewById(R.id.textKeywordItem);
        }
    }
}
