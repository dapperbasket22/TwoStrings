package pelicula.shiri.twostrings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import pelicula.shiri.twostrings.R;
import pelicula.shiri.twostrings.model.CastObject;
import pelicula.shiri.twostrings.utilities.TMAUrl;

public class CastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<CastObject> mDataSet;
    private ImageLoader mImgLoader;

    public CastAdapter(ArrayList<CastObject> data, ImageLoader loader) {
        mDataSet = data;
        mImgLoader = loader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_cast, parent, false);
        return new ViewHolderCast(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CastObject object = mDataSet.get(position);
        ViewHolderCast holderCast = (ViewHolderCast) holder;

        String profile = TMAUrl.IMAGE_LOW_URL + object.getmProfile();
        holderCast.imageProfile.setImageUrl(profile, mImgLoader);
        holderCast.textName.setText(object.getmName());
        holderCast.textCharacter.setText(object.getmCharacter());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private class ViewHolderCast extends RecyclerView.ViewHolder {
        NetworkImageView imageProfile;
        TextView textName, textCharacter;

        ViewHolderCast(View itemView) {
            super(itemView);
            imageProfile = (NetworkImageView) itemView.findViewById(R.id.imageCastItem);
            textName = (TextView) itemView.findViewById(R.id.textCastItemName);
            textCharacter = (TextView) itemView.findViewById(R.id.textCastItemChar);
        }
    }
}
