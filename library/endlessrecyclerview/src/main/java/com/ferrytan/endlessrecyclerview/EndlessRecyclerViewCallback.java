package com.ferrytan.endlessrecyclerview;

/**
 * Created by ferrytan on 9/7/16.
 */
public interface EndlessRecyclerViewCallback {
    void readyToLoadData();
    void failedToLoadData(String message);
}
