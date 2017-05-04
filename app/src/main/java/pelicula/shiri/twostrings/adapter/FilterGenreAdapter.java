package pelicula.shiri.twostrings.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.model.TdObject;

public class FilterGenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TdObject> mDataset;
    private Context mContext;
    private ArrayList<Integer> mSelectionType;

    private static final int SELECTION_NONE = 0;
    private static final int SELECTION_WITH = 1;
    private static final int SELECTION_WITHOUT = -1;


    public FilterGenreAdapter(ArrayList<TdObject> data, Context context) {
        mDataset = data;
        mContext = context;
        mSelectionType = new ArrayList<>(Collections.nCopies(18,0));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_filter_genre, parent, false);
        return new ViewHolderFilterGenre(v);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ViewHolderFilterGenre holderFilterGenre = (ViewHolderFilterGenre) holder;
        final TdObject object = mDataset.get(position);
        int defaultType = mSelectionType.get(position);
        if (defaultType == SELECTION_NONE)
            holderFilterGenre.layoutGenre.setBackgroundColor(mContext.getResources().
                    getColor(R.color.cardview_dark_background));
        else if (defaultType == SELECTION_WITH)
            holderFilterGenre.layoutGenre.setBackgroundColor(mContext.getResources().
                    getColor(R.color.check));
        else holderFilterGenre.layoutGenre.setBackgroundColor(mContext.getResources().
                    getColor(R.color.uncheck));

        holderFilterGenre.textName.setText(object.getmName());

        holderFilterGenre.frameCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = mSelectionType.get(holder.getAdapterPosition());
                mSelectionType.remove(holder.getAdapterPosition());
                if (type == SELECTION_WITH) {
                    mSelectionType.add(holder.getAdapterPosition(), SELECTION_NONE);
                    holderFilterGenre.layoutGenre.setBackgroundColor(mContext.getResources().
                            getColor(R.color.cardview_dark_background));
                } else {
                    mSelectionType.add(holder.getAdapterPosition(), SELECTION_WITH);
                    holderFilterGenre.layoutGenre.setBackgroundColor(mContext.getResources().
                            getColor(R.color.check));
                }
            }
        });
        holderFilterGenre.frameUncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = mSelectionType.get(holder.getAdapterPosition());
                mSelectionType.remove(holder.getAdapterPosition());
                if (type == SELECTION_WITHOUT) {
                    mSelectionType.add(holder.getAdapterPosition(), SELECTION_NONE);
                    holderFilterGenre.layoutGenre.setBackgroundColor(mContext.getResources().
                            getColor(R.color.cardview_dark_background));
                } else {
                    mSelectionType.add(holder.getAdapterPosition(), SELECTION_WITHOUT);
                    holderFilterGenre.layoutGenre.setBackgroundColor(mContext.getResources().
                            getColor(R.color.uncheck));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private class ViewHolderFilterGenre extends RecyclerView.ViewHolder {
        TextView textName;
        ConstraintLayout layoutGenre;
        FrameLayout frameCheck, frameUncheck;

        ViewHolderFilterGenre(View itemView) {
            super(itemView);
            layoutGenre = (ConstraintLayout) itemView.findViewById(R.id.layoutFilterGenre);
            frameCheck = (FrameLayout) itemView.findViewById(R.id.layoutCheck);
            frameUncheck = (FrameLayout) itemView.findViewById(R.id.layoutUncheck);
            textName = (TextView) itemView.findViewById(R.id.textFilterGenreName);
        }
    }

    public ArrayList<Integer> getmSelectionType() {
        return mSelectionType;
    }
}
