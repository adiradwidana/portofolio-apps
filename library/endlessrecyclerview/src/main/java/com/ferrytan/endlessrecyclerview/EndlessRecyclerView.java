package com.ferrytan.endlessrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import java.io.InvalidClassException;
import java.util.List;

/**
 * Created by ferrytan on 9/7/16.
 */
public class EndlessRecyclerView extends RecyclerView {
    public static final boolean DEBUG = true;
    @LayoutRes private static final int DEFAULT_ERROR_LAYOUT = R.layout.endless_rv_error;
    @LayoutRes private static final int DEFAULT_LOADING_LAYOUT = R.layout.endless_rv_loading;
    @LayoutRes private static final int DEFAULT_WAITING_LAYOUT = R.layout.endless_rv_waiting;
    @IdRes private static final int DEFAULT_ERROR_TEXT_ID = R.id.txv_error;
    @IdRes private static final int DEFAULT_WAITING_ACTION_VIEW_ID = R.id.txv_load;
    @IdRes private static final int DEFAULT_ERROR_ACTION_VIEW_ID = R.id.btn_error;
    private static final int DEFAULT_VISIBLE_THRESHOLD = 0;
    private boolean mLoadMoreDataEnabled, mAutoLoad;
    private int mVisibleThreshold;
    private static final String TAG = "EndlessRecyclerView";
    @LayoutRes  private int mErrorLayoutRes, mLoadingLayoutRes, mWaitingToLoadLayoutRes;
    @IdRes private int mErrorTextViewId, mErrorActionViewId, mWaitingToLoadTextViewId;
    private String mLoadMoreMessage;
    private EndlessRecyclerViewScrollListener mEndlessScrollListener;

    public EndlessRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public EndlessRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EndlessRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.EndlessRecyclerView);

        mVisibleThreshold = a.getInt(R.styleable.EndlessRecyclerView_visibleThreshold, DEFAULT_VISIBLE_THRESHOLD);
        mLoadMoreDataEnabled = a.getBoolean(R.styleable.EndlessRecyclerView_loadMoreEnabled, true);
        mAutoLoad = a.getBoolean(R.styleable.EndlessRecyclerView_autoLoad, true);
        mErrorLayoutRes = a.getResourceId(R.styleable.EndlessRecyclerView_errorLayout, DEFAULT_ERROR_LAYOUT);
        mLoadingLayoutRes = a.getResourceId(R.styleable.EndlessRecyclerView_loadingLayout, DEFAULT_LOADING_LAYOUT);
        mWaitingToLoadLayoutRes = a.getResourceId(R.styleable.EndlessRecyclerView_waitingToLoadLayout, DEFAULT_WAITING_LAYOUT);
        mWaitingToLoadTextViewId = a.getResourceId(R.styleable.EndlessRecyclerView_waitingToLoadActionViewId, DEFAULT_WAITING_ACTION_VIEW_ID);
        mErrorTextViewId = a.getResourceId(R.styleable.EndlessRecyclerView_errorTextViewId, DEFAULT_ERROR_TEXT_ID);
        mErrorActionViewId = a.getResourceId(R.styleable.EndlessRecyclerView_errorActionViewId, DEFAULT_ERROR_ACTION_VIEW_ID);

        a.recycle();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        try {
            setEndlessAdapter(adapter);
        } catch (InvalidClassException e) {
            e.printStackTrace();
        } finally {
            if(DEBUG) Log.d(TAG, "setAdapter: addOnScrollListener");
            mEndlessScrollListener = new EndlessRecyclerViewScrollListener() {

                @Override
                public void onScrolledToBottom() {
                    if(DEBUG) Log.d(TAG, "onScrolledToBottom:");
                    loadData(EndlessRecyclerViewAdapter.TYPE_BELOW);
                }

                @Override
                public void onScrolledToTop() {
                    // TODO never gets called
                    loadData(EndlessRecyclerViewAdapter.TYPE_ABOVE);
                }

                @Override
                public void onReachedEndOfData() {
                    // TODO what condition to put here? to reset the scroll listener
                    if(DEBUG) Log.d(TAG, "onReachedEndOfData: ");
                    try {
                        if (getEndlessAdapter().isIdle()) {
                            resetScroll();
                        }
                    } catch (RuntimeException re) {
                        re.printStackTrace();
                    }
                }

                @Override
                public int getVisibleThreshold() {
                    return mVisibleThreshold;
                }
            };
            addOnScrollListener(mEndlessScrollListener);

            setErrorLayout(mErrorLayoutRes, mErrorTextViewId, mErrorActionViewId);
            setLoadingLayout(mLoadingLayoutRes);
            setWaitingToLoadLayout(mWaitingToLoadLayoutRes, mWaitingToLoadTextViewId);
            mLoadMoreMessage = getResources().getString(R.string.default_waiting_message);
        }
    }

    /**
     * set visible threshold to tell the RV that it has reached bottom of the list
     * @param visibleThreshold if (lastVisibleIndex == (lastItemIndex - visibleThreshold)) means that items before last means list has reached it's bottom
     */
    public void setVisibleThreshold(int visibleThreshold) {
        mVisibleThreshold = visibleThreshold;
    }


    /**
     * @return adapter casted as EndlessRecyclerViewAdapter
     */
    private EndlessRecyclerViewAdapter getEndlessAdapter() throws RuntimeException {
        try {
            return (EndlessRecyclerViewAdapter) getAdapter();
        } catch (ClassCastException e) {
            throw new RuntimeException("You must call setAdapter() first before accessing EndlessRecyclerViewAdapter methods");
        }
    }

    /**
     * set the adapter as EndlessRecyclerViewAdapter
     *
     * @param endlessAdapter the adapter to be set
     * @throws InvalidClassException
     */
    private void setEndlessAdapter(Adapter endlessAdapter) throws InvalidClassException {
        if (endlessAdapter instanceof EndlessRecyclerViewAdapter)
            super.setAdapter(endlessAdapter);
        else
            throw new InvalidClassException("Adapter must extend EndlessRecyclerViewAdapter");
    }

    /**
     * set a custom loading layout
     *
     * @param layoutRes
     */
    public void setLoadingLayout(@LayoutRes int layoutRes) {
        mLoadingLayoutRes = layoutRes;
        try {
            getEndlessAdapter().setLoadingLayout(mLoadingLayoutRes);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * set a custom error layout with message & retry button
     *
     * @param layoutRes           id res of the error layout
     * @param messageTextViewResId  id res of error message TextView, set NONE (@see{@link ErrorLoadingLayout#RES_ID_NULL}) if there's no error message on the layout
     * @param retryButtonResId      id res of retry Button, set NONE (@see{@link ErrorLoadingLayout#RES_ID_NULL}) if there's no retry button on the layout
     */
    public void setErrorLayout(@LayoutRes int layoutRes,
                               @IdRes int messageTextViewResId,
                               @IdRes int retryButtonResId) {
        mErrorLayoutRes = layoutRes;
        mErrorTextViewId = messageTextViewResId;
        mErrorActionViewId = retryButtonResId;
        try {
            getEndlessAdapter().setErrorLayout(mErrorLayoutRes, mErrorTextViewId, mErrorActionViewId);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * set a custom waitingToLoad layout with message & retry button
     *
     * @param layoutRes           id res of the waitingToLoad layout
     * @param messageTextViewResId  id res of waiting message TextView, set NONE (@see{@link ErrorLoadingLayout#RES_ID_NULL}) if there's no waiting message on the layout
     */
    public void setWaitingToLoadLayout(@LayoutRes int layoutRes,
                               @IdRes int messageTextViewResId) {
        mWaitingToLoadLayoutRes = layoutRes;
        mWaitingToLoadTextViewId = messageTextViewResId;
        try {
            getEndlessAdapter().setWaitingToLoadLayout(mWaitingToLoadLayoutRes, mWaitingToLoadTextViewId);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * true if RV is able to load more data
     * @return
     */
    public boolean canLoadMoreData() {
        if(DEBUG) Log.d(TAG, "canLoadMoreData: " + mLoadMoreDataEnabled);
        return mLoadMoreDataEnabled;
    }

    /**
     * set whether the RV can load more data or not, use if API response notify that there's no more data to fetch
     * so RV don't need to send callback to load more data
     *
     * @param loadMoreDataEnabled
     */
    public void setLoadMoreDataEnabled(boolean loadMoreDataEnabled) {
        mLoadMoreDataEnabled = loadMoreDataEnabled;
    }
    /**
     * set whether the RV is loading the data automatically or not
     *
     * @param autoLoad
     */
    public void setAutoLoad(boolean autoLoad) {
        mAutoLoad = autoLoad;
    }

    public boolean isAutoLoad() {
        return mAutoLoad;
    }

    /**
     * tell the adapter to show error layout
     * @param message error message to show (if available)
     */
    public void showError(String message){
        try {
            getEndlessAdapter().errorLoadingData(message);
            final int lastIndex = getEndlessAdapter().getItemCount();
            getLayoutManager().smoothScrollToPosition(EndlessRecyclerView.this, null, lastIndex);
        }catch (RuntimeException re){
            re.printStackTrace();
        }
    }


    /**
     * tell the adapter to show waiting to load layout
     * @param message waitingToLoad message to show (if available)
     */
    public void setLoadMessage(String message){
        mLoadMoreMessage = message;
    }

    /**
     * tell the adapter to finish loading & add items
     * @param items
     */
    public void addItems(List items){
        try{
            getEndlessAdapter().addItems(items);
        }catch (RuntimeException re){
            re.printStackTrace();
        }
    }

    /**
     * tell the adapter to finish loading & add item on position
     * @param position position to add
     * @param item item to add
     */
    @SuppressWarnings("unchecked")
    public void addItem(int position, Object item){
        try{
            getEndlessAdapter().addItem(position, item);
        }catch (RuntimeException re){
            re.printStackTrace();
        }
    }

    /**
     * tell the adapter to finish loading data
     */
    public void finishLoadingData(){
        try{
            getEndlessAdapter().loadingMoreDataFinished();
            mEndlessScrollListener.resetScroll();
        }catch (RuntimeException re){
            re.printStackTrace();
        }
    }

    /**
     * tell the adapter to clear all data
     */
    public void clearData(){
        try {
            getEndlessAdapter().clearData();
            mEndlessScrollListener.resetScroll();
        }catch (RuntimeException re){
            re.printStackTrace();
        }
    }

    /**
     * tell the adapter to load more data
     */
    public void loadData(){
        if (canLoadMoreData()) {
            try {
                if(mAutoLoad || getEndlessAdapter().getItemCount()==0)
                    getEndlessAdapter().prepareToLoadMoreData(EndlessRecyclerViewAdapter.TYPE_BELOW, false);
                else
                    getEndlessAdapter().waitingToLoadMoreData(EndlessRecyclerViewAdapter.TYPE_BELOW, mLoadMoreMessage);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
        }
    }

    /**
     * tell the adapter to load more data by type
     * @param type type to load
     */
    public void loadData(final int type){
        if (canLoadMoreData()) {
            try {
                if(mAutoLoad || getEndlessAdapter().getItemCount()==0)
                    getEndlessAdapter().prepareToLoadMoreData(type, false);
                else
                    getEndlessAdapter().waitingToLoadMoreData(type, mLoadMoreMessage);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
        }
    }

    /**
     *
     * @return data count in adapter
     */
    public int getDataCount(){
        try {
            return getEndlessAdapter().getDataCount();
        }catch (RuntimeException re){
            re.printStackTrace();
        }
        return 0;
    }

    /**
     * tell the adapter to remove item in position
     * @param position item to remove
     */
    public void removeItem(int position){
        try {
            getEndlessAdapter().removeItem(position);
        }catch (RuntimeException re){
            re.printStackTrace();
        }
    }

    /**
     * tell the adapter to update item in position
     * @param position item to remove
     */
    @SuppressWarnings("unchecked")
    public void updateItem(int position, Object newObject){
        try {
            getEndlessAdapter().updateItem(position, newObject);
        }catch (RuntimeException re){
            re.printStackTrace();
        }
    }

    /**
     * reset all adapter items to list
     * @param objects
     */
    public void resetItemsToList(List objects){
        try {
            getEndlessAdapter().resetItemToList(objects);
        }catch (RuntimeException re){
            re.printStackTrace();
        }
    }
}
