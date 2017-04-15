package pelicula.shiri.twostrings.utilities;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class StartOffsetDecoration extends RecyclerView.ItemDecoration {
    private final int size;

    public StartOffsetDecoration(int sz) {
        size = sz;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left += size;
        }
    }
}
