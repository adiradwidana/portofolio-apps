package com.base.app.domain.ui.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by cahaya on 11/3/16.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int spanCount;
    private int mTop;

    public SpaceItemDecoration(int spanCount, int space, int top) {
        this.space = space;
        this.spanCount = spanCount;
        this.mTop = top;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);
        if (position < spanCount){
            outRect.top = mTop;
        }

        if ((position + 1) % 2 == 0){
            outRect.left = space / 2;
            outRect.right = space;
        } else {
            outRect.left = space;
            outRect.right = space / 2;
        }

        outRect.bottom = mTop;
    }
}

