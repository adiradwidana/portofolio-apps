package com.ferrytan.endlessrecyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private int previousTotal = 0;
    private boolean loading = false;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = recyclerView.getChildCount();
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        if(firstVisibleItem<0) firstVisibleItem = 0;

        if (loading) {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
//                    totalItemCount-=1; // exclude loading footer
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }else{
                        onReachedEndOfData();
                    }
                }
            });
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + getVisibleThreshold())) {
            // End has been reached
            previousTotal = totalItemCount;
            loading = true;
            this.onScrolledToBottom();
        }
    }

    public abstract void onScrolledToBottom();
    public abstract void onScrolledToTop();
    public abstract void onReachedEndOfData();
    public abstract int getVisibleThreshold();

    public void resetScroll() {
        previousTotal = 0;
        loading = false;
    }

    public void lockScrolling(){
        loading = true;
        previousTotal = Integer.MAX_VALUE;
    }
}
