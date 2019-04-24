package com.base.app.domain.ui.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by taufanerfiyanto on 6/2/17 for kampung-berkah.
 */

public class HistoryItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public HistoryItemOffsetDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public HistoryItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0 ) {
            outRect.set((mItemOffset * 2), (mItemOffset * 2), (mItemOffset * 2), mItemOffset );
        } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.set((mItemOffset * 2), mItemOffset, (mItemOffset * 2), (mItemOffset * 2));
        } else {
            outRect.set((mItemOffset * 2), mItemOffset, (mItemOffset * 2), mItemOffset);
        }
    }
}