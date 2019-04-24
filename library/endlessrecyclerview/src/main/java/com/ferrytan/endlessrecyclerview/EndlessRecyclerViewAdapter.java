package com.ferrytan.endlessrecyclerview;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

public abstract class EndlessRecyclerViewAdapter<DVH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEWTYPE_DATA = 90;
    public static final int VIEWTYPE_OTHER = 91;
    public static final int TYPE_BELOW = 1;
    public static final int TYPE_ABOVE = 2;
    public static final int STATE_LOADING = 21;
    public static final int STATE_WAITING = 22;
    public static final int STATE_ERROR = 23;
    public static final int STATE_IDLE = 20;
    protected final LinkedList<T> data;
    private final Context mContext;
    private EndlessRecyclerViewCallback mEndlessRecyclerViewCallback;
    private int mLoadMoreType;
    private int mItemCount;
    private int mState;
    private String mErrorMessage, mWaitingToLoadMessage;
    @LayoutRes private int mErrorLayoutRes, mLoadingLayoutRes, mWaitingToLoadLayoutRes;
    @IdRes private int mErrorTextViewIdRes, mErrorActionViewIdRes, mWaitingToLoadTextViewIdRes;
    private String TAG = getClass().getSimpleName();
    private boolean mUseCustomErrorLoadingLayout;
    private Handler mHandler;

    @SuppressWarnings("unchecked")
    protected EndlessRecyclerViewAdapter(Context context, List<T> data, EndlessRecyclerViewCallback callback) {
        this.mContext = context;
        this.data = new LinkedList(data);
        this.mEndlessRecyclerViewCallback = callback;
        mHandler = new Handler();
    }

    public Context getContext() {
        return mContext;
    }

    protected abstract DVH onCreateDataViewHolder(ViewGroup parent, int viewType);
    protected abstract void onBindDataViewHolder(DVH holder, int position);
    protected abstract int getHeaderCount();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_OTHER) {
            if(EndlessRecyclerView.DEBUG) Log.d(TAG, "onCreateViewHolder: OTHER");
            ErrorLoadingLayout errorLoadingLayout = new ErrorLoadingLayout(mContext);
            errorLoadingLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if(mUseCustomErrorLoadingLayout) {
                errorLoadingLayout.setErrorLayout(mErrorLayoutRes, mErrorTextViewIdRes, mErrorActionViewIdRes);
                errorLoadingLayout.setLoadingLayout(mLoadingLayoutRes);
                errorLoadingLayout.setWaitingToLoadLayout(mWaitingToLoadLayoutRes, mWaitingToLoadTextViewIdRes);
                errorLoadingLayout.updateView();
                onCustomErrorLoadingLayout(errorLoadingLayout);
            }
            return new OtherViewHolder(errorLoadingLayout);
        }else {
            if(EndlessRecyclerView.DEBUG) Log.d(TAG, "onCreateViewHolder: DATA");
            return onCreateDataViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position<data.size()+getHeaderCount()) {
            if(EndlessRecyclerView.DEBUG) Log.d(TAG, "onBindDataViewHolder: pos - " + position);
            this.onBindDataViewHolder((DVH) holder, position);
        }else{
            // TODO not data view holder
            if(EndlessRecyclerView.DEBUG) Log.d(TAG, "onBindOtherViewHolder: pos - " + position);
            ((OtherViewHolder)holder).updateViewHolder(mState);
        }
    }

    @Override
    public int getItemCount() {
        calculateGetItemCount();
        return mItemCount;
    }

    /**
     * calculate adapter item including loading/error view
     */
    private void calculateGetItemCount(){
        mItemCount = data.size();
        if (mState!=STATE_IDLE)
            mItemCount+=1;
    }

    /**
     * set a custom error layout with message & retry button
     *
     * @param errorLayoutRes           id res of the error layout
     * @param errorTextViewIdRes  id res of error message TextView, set NONE (@see{@link ErrorLoadingLayout#RES_ID_NULL}) if there's no error message on the layout
     * @param errorActionViewIdRes      id res of retry action view, set NONE (@see{@link ErrorLoadingLayout#RES_ID_NULL}) if there's no retry action view on the layout
     */
    public final void setErrorLayout(@LayoutRes int errorLayoutRes, @IdRes int errorTextViewIdRes, @IdRes int errorActionViewIdRes) {
        mErrorLayoutRes = errorLayoutRes;
        mErrorTextViewIdRes = errorTextViewIdRes;
        mErrorActionViewIdRes = errorActionViewIdRes;
        mUseCustomErrorLoadingLayout = true;
    }
    /**
     * set a custom waiting to load layout with message & retry button
     *
     * @param waitingToLoadLayoutRes        id res of the waiting to load layout
     * @param waitingToLoadActionViewIdRes  id res of waiting to load message TextView, set NONE (@see{@link ErrorLoadingLayout#RES_ID_NULL}) if there's no action message on the layout
     */
    public final void setWaitingToLoadLayout(@LayoutRes int waitingToLoadLayoutRes, @IdRes int waitingToLoadActionViewIdRes) {
        mWaitingToLoadLayoutRes = waitingToLoadLayoutRes;
        mWaitingToLoadTextViewIdRes = waitingToLoadActionViewIdRes;
        mUseCustomErrorLoadingLayout = true;
    }

    /**
     * set a custom loading layout
     *
     * @param loadingLayoutRes
     */
    public final void setLoadingLayout(@LayoutRes int loadingLayoutRes) {
        if(EndlessRecyclerView.DEBUG) Log.d(TAG, "setLoadingLayout() called with: " + "loadingLayoutRes = [" + loadingLayoutRes + "]");
        mLoadingLayoutRes = loadingLayoutRes;
        mUseCustomErrorLoadingLayout = true;
    }

    @Override
    public int getItemViewType(int position) {
        if (position>=data.size()+getHeaderCount()) {
            return VIEWTYPE_OTHER;
        }
        return VIEWTYPE_DATA;
    }

    /**
     * prepare a loading viewholder then call the callback to load more data
     * @param type loading type below {@link #TYPE_BELOW} and {@link #TYPE_ABOVE}
     */
    public final void prepareToLoadMoreData(@IntRange(from = TYPE_BELOW, to = TYPE_ABOVE) final int type, final boolean fromRetry) {
        if(mState!=STATE_LOADING) {
            if(EndlessRecyclerView.DEBUG) Log.d(TAG, "prepareToLoadMoreData: ");
            mLoadMoreType = type;
            mState = STATE_LOADING;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(getDataCount()>0){
                        if(fromRetry){
                            notifyItemChanged(getDataCount());
                        }else {
                            notifyItemInserted(getDataCount());
                        }
                    }
                    mEndlessRecyclerViewCallback.readyToLoadData();
                }
            });
        }else{
            if(EndlessRecyclerView.DEBUG) Log.d(TAG, "prepareToLoadMoreData: still loading data");
        }
    }

    /**
     * prepare a waiting to load viewholder then call the callback to load more data
     * @param type loading type below {@link #TYPE_BELOW} and {@link #TYPE_ABOVE}
     */
    public final void waitingToLoadMoreData(@IntRange(from = TYPE_BELOW, to = TYPE_ABOVE) final int type, final String waitingToLoadMessage) {
        if(mState!=STATE_WAITING) {
            mState = STATE_WAITING;
            mLoadMoreType = type;
            mWaitingToLoadMessage = waitingToLoadMessage;

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    switch (mLoadMoreType) {
                        case TYPE_ABOVE:
                            break;
                        case TYPE_BELOW:
                            int lastIndex = data.size()+getHeaderCount();
                            notifyItemChanged(lastIndex);
                        default:
                            break;
                    }
                }
            });
        }else{
            if(EndlessRecyclerView.DEBUG) Log.d(TAG, "waitingToLoadMoreData: still waiting to load data");
        }
    }

    /**
     * show error layout
     * @param message error message to show
     */
    public final void errorLoadingData(final String message){
        if(mState!=STATE_ERROR){
            if(EndlessRecyclerView.DEBUG) Log.d(TAG, "errorLoadingData: showing error view");
            mState = STATE_ERROR;

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    switch (mLoadMoreType) {
                        case TYPE_ABOVE:
                            // TODO above
                            break;
                        case TYPE_BELOW:
                            int lastIndex = data.size()+getHeaderCount();
                            notifyItemChanged(lastIndex);
                        default:
                            break;
                    }
                    mErrorMessage = message;
                    mEndlessRecyclerViewCallback.failedToLoadData(mErrorMessage);
                }
            });
        }else{
            if(EndlessRecyclerView.DEBUG) Log.d(TAG, "errorLoadingData: already showing error view");
        }
    }

    /**
     * remove loading indicator
     */
    public final void loadingMoreDataFinished() {
        if(mState == STATE_LOADING) {
            mState = STATE_IDLE;
            switch (mLoadMoreType) {
                case TYPE_BELOW:
                    final int lastIndex = data.size()+getHeaderCount();
                    if(EndlessRecyclerView.DEBUG) Log.d(TAG, "loadingMoreDataFinished: notify item removed on last index " + lastIndex);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemRemoved(lastIndex);
                        }
                    });
                    break;
                case TYPE_ABOVE:
                    /** use this if we want to add a custom loading view above the list**/
                    //TODO above
                /*
                this.notifyItemChanged(0);*/
                    break;
            }
        }
    }

    /**
     * add items to list & finish loading more data
     * TODO must notify child class not to override this class
     * @param newItems
     */
    @SuppressWarnings("unchecked")
    public final void addItems(List newItems){
        int start;
        switch (mLoadMoreType){
            case TYPE_BELOW:
                start = data.size()+1+getHeaderCount();
                data.addAll(newItems);
                break;
            case TYPE_ABOVE:
                start = 0;
                data.addAll(start, newItems);
                break;
            default:
                return;
        }
        notifyItemRangeInserted(start, newItems.size());
        loadingMoreDataFinished();
    }

    public final void addItem(int position, T object){
        data.add(position, object);
        notifyItemInserted(position);
        loadingMoreDataFinished();
    }

    /**
     * remove item on index
     * @param position to remove
     */
    public void removeItem(int position){
        if(position>=0 && position<getDataCount()) {
            data.remove(position);
            notifyItemRemoved(position);
        }else {
            if(EndlessRecyclerView.DEBUG) Log.d(TAG, "removeItem: invalid index");
        }
    }

    /**
     * remove item on index
     * @param position to remove
     */
    public void updateItem(int position, T object){
        if(position>=0 && position<getDataCount()) {
            data.set(position, object);
            notifyItemChanged(position);
        }else {
            if(EndlessRecyclerView.DEBUG) Log.d(TAG, "removeItem: invalid index");
        }
    }

    /**
     * clear all list items and reset it with newItems
     * @param newItems
     */
    public final void resetItemToList(List newItems){
        mLoadMoreType = TYPE_BELOW;
        data.clear();
        notifyDataSetChanged();
        addItems(newItems);
    }

    /**
     * @return true if list is on IDLE state
     */
    public boolean isIdle(){
        return mState == STATE_IDLE;
    }

    /**
     *
     * @return data count
     */
    public final int getDataCount(){
        return data.size();
    }

    public final void clearData(){
        data.clear();
        notifyDataSetChanged();
    }

    protected abstract void onCustomErrorLoadingLayout(ErrorLoadingLayout errorLoadingLayout);

    /**
     * ViewHolder class for Error/Loading layout
     */
    public class OtherViewHolder extends RecyclerView.ViewHolder {

        public OtherViewHolder(View itemView) {
            super(itemView);
            updateViewHolder(mState);
        }

        /**
         * update viewholder layout to loading/error
         * @param state loading layout {@link #STATE_LOADING} or error {@link #STATE_ERROR}
         */
        public void updateViewHolder(@IntRange (from = STATE_LOADING, to = STATE_ERROR) int state){
            switch (state){
                case STATE_LOADING:
                    ((ErrorLoadingLayout)itemView).switchLayout(ErrorLoadingLayout.POS_LOADING);
                    onCustomErrorLoadingLayout(((ErrorLoadingLayout)itemView));
                    break;
                case STATE_WAITING:
                    ((ErrorLoadingLayout)itemView).switchLayout(ErrorLoadingLayout.POS_WAITING);
                    ((ErrorLoadingLayout)itemView).setWaitingToLoadMessage(mWaitingToLoadMessage);
                    ((ErrorLoadingLayout)itemView).setOnWaitingToLoadClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateViewHolder(STATE_LOADING);
                            prepareToLoadMoreData(mLoadMoreType, true);
                        }
                    });
                    break;
                case STATE_ERROR:
                    ((ErrorLoadingLayout)itemView).switchLayout(ErrorLoadingLayout.POS_ERROR);
                    ((ErrorLoadingLayout) itemView).setErrorMessage(mErrorMessage);
                    ((ErrorLoadingLayout) itemView).setOnErrorClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateViewHolder(STATE_LOADING);
                            prepareToLoadMoreData(mLoadMoreType, true);
                        }
                    });
                    break;
                default:
                    if(EndlessRecyclerView.DEBUG) Log.d(TAG, "updateViewHolder() called with false state: " + "state = [" + state + "]");
            }
        }
    }
}
