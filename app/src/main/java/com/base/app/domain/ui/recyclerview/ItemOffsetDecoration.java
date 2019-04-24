package com.base.app.domain.ui.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by taufan on 21/03/2018.
 */

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private int itemLeftRightOffset;
    private int itemTopBottomOffset;

    public ItemOffsetDecoration(int itemLeftRightOffset, int itemTopBottomOffset) {
        this.itemLeftRightOffset = itemLeftRightOffset;
        this.itemTopBottomOffset = itemTopBottomOffset;
    }

    public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemLeftRightOffset,
                                @DimenRes int itemTopBottomOffset) {
        this(context.getResources().getDimensionPixelSize(itemLeftRightOffset),
                context.getResources().getDimensionPixelSize(itemTopBottomOffset));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(itemLeftRightOffset, itemTopBottomOffset, itemLeftRightOffset, itemTopBottomOffset);

        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.set(itemLeftRightOffset, itemTopBottomOffset, itemLeftRightOffset, (itemTopBottomOffset + itemTopBottomOffset));
        } else if (parent.getChildAdapterPosition(view) == 0) {
            outRect.set(itemLeftRightOffset, (itemTopBottomOffset + itemTopBottomOffset), itemLeftRightOffset, itemTopBottomOffset);
        } else {
            outRect.set(itemLeftRightOffset, itemTopBottomOffset, itemLeftRightOffset, itemTopBottomOffset);
        }
    }
}