package com.ferrytan.endlessrecyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by ferrytan on 9/13/16.
 */
public class ErrorLoadingLayout extends FrameLayout {
    public static final int POS_ERROR= 0;
    public static final int POS_LOADING= 1;
    public static final int POS_WAITING= 2;
    public static final int RES_ID_NULL = -1;

    private View mLoadingLayout, mErrorLayout, mWaitingToLoadLayout;
    private int mErrorTextViewIdRes, mErrorActionViewIdRes, mWaitingToLoadActionViewIdRes;

    public ErrorLoadingLayout(Context context) {
        super(context);
    }

    public ErrorLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ErrorLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * set error message to show in error layout
     * @param errorMessage the error message
     */
    public void setErrorMessage(String errorMessage){
        try{
            trySetErrorMessage(errorMessage);
        }catch (Resources.NotFoundException nfe){
            nfe.printStackTrace();
        }catch (ClassCastException cce){
            cce.printStackTrace();
        }
    }

    /**
     * set error message to show in error layout
     * @param waitingMessage the error message
     */
    public void setWaitingToLoadMessage(String waitingMessage){
        try{
            trySetWaitingToLoadMessage(waitingMessage);
        }catch (Resources.NotFoundException nfe){
            nfe.printStackTrace();
        }catch (ClassCastException cce){
            cce.printStackTrace();
        }
    }

    /**
     * try setting error message, to handle exceptions
     * @param errorMessage the error message
     * @throws Resources.NotFoundException if the id is not found on the layout
     * @throws ClassCastException if the id is not a TextView
     */
    private void trySetErrorMessage(String errorMessage) throws Resources.NotFoundException, ClassCastException{
        if(mErrorTextViewIdRes!=RES_ID_NULL) {
            ((TextView) getChildAt(POS_ERROR).findViewById(mErrorTextViewIdRes)).setText(errorMessage);
        }else{
            throw new Resources.NotFoundException("Have you declared an error TextView on your layout?");
        }
    }

    /**
     * try setting error message, to handle exceptions
     * @param waitingMessage the waiting message
     * @throws Resources.NotFoundException if the id is not found on the layout
     * @throws ClassCastException if the id is not a TextView
     */
    private void trySetWaitingToLoadMessage(String waitingMessage) throws Resources.NotFoundException, ClassCastException{
        if(mWaitingToLoadActionViewIdRes !=RES_ID_NULL) {
            ((TextView) getChildAt(POS_WAITING).findViewById(mWaitingToLoadActionViewIdRes)).setText(waitingMessage);
        }else{
            throw new Resources.NotFoundException("Have you declared a waiting TextView on your layout?");
        }
    }

    /**
     * set a custom error layout with message & retry button & refresh the view
     *
     * @param errorLayoutRes        id res of the error layout
     * @param errorTextViewIdRes    id res of error message TextView, set NONE (@see{@link ErrorLoadingLayout#RES_ID_NULL}) if there's no error message on the layout
     * @param errorButtonIdRes      id res of retry Button, set NONE (@see{@link ErrorLoadingLayout#RES_ID_NULL}) if there's no retry button on the layout
     */
    public void setErrorLayout(@LayoutRes int errorLayoutRes, @IdRes int errorTextViewIdRes, @IdRes int errorButtonIdRes) {
        if(BuildConfig.DEBUG) Log.d(getClass().getSimpleName(), "setErrorLayout() called with: " + "errorLayoutRes = [" + errorLayoutRes + "], errorTextViewIdRes = [" + errorTextViewIdRes + "], errorButtonIdRes = [" + errorButtonIdRes + "]");
        mErrorLayout = LayoutInflater.from(getContext()).inflate(errorLayoutRes, null);
        mErrorTextViewIdRes = errorTextViewIdRes;
        mErrorActionViewIdRes = errorButtonIdRes;
    }

    /**
     * show or hide error message
     * @param show
     */
    public void setErrorMessageEnabled(boolean show){
        if(mErrorLayout!=null && mErrorTextViewIdRes!=RES_ID_NULL) {
            mErrorLayout.findViewById(mErrorTextViewIdRes).setVisibility(show ? VISIBLE : GONE);
        }
    }

    /**
     * set a simple custom error layout
     *
     * @param errorLayoutRes        id res of the error layout
     */
    public void setErrorLayout(@LayoutRes int errorLayoutRes){
        mErrorLayout = LayoutInflater.from(getContext()).inflate(errorLayoutRes, null);
        mErrorTextViewIdRes = RES_ID_NULL;
        mErrorActionViewIdRes = RES_ID_NULL;
    }

    /**
     * set a custom error layout with message & retry button & refresh the view
     *
     * @param waitingToLoadLayoutRes        id res of the waitingToLoad layout
     * @param waitingToLoadTextViewIdRes    id res of waitingToLoad message TextView, set NONE (@see{@link ErrorLoadingLayout#RES_ID_NULL}) if there's no message on the layout
     */
    public void setWaitingToLoadLayout(@LayoutRes int waitingToLoadLayoutRes, @IdRes int waitingToLoadTextViewIdRes) {
        mWaitingToLoadLayout = LayoutInflater.from(getContext()).inflate(waitingToLoadLayoutRes, null);
        mWaitingToLoadActionViewIdRes = waitingToLoadTextViewIdRes;
    }

    /**
     * set a simple custom error layout
     *
     * @param waitingToLoadLayoutRes        id res of the error layout
     */
    public void setWaitingToLoadLayout(@LayoutRes int waitingToLoadLayoutRes){
        mWaitingToLoadLayout = LayoutInflater.from(getContext()).inflate(waitingToLoadLayoutRes, null);
        mWaitingToLoadActionViewIdRes = RES_ID_NULL;
    }

    /**
     * set a custom loading layout
     * @param loadingLayoutRes
     */
    public void setLoadingLayout(@LayoutRes int loadingLayoutRes){
        if(BuildConfig.DEBUG) Log.d(getClass().getSimpleName(), "setLoadingLayout() called with: " + "loadingLayoutRes = [" + loadingLayoutRes + "]");
        mLoadingLayout = LayoutInflater.from(getContext()).inflate(loadingLayoutRes, null);
    }

    /**
     * re-attach the views
     */
    public void updateView(){
        removeAllViews();
//        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        if(mErrorLayout!=null) addView(mErrorLayout/*, params*/);
        if(mLoadingLayout!=null) addView(mLoadingLayout/*, params*/);
        if(mWaitingToLoadLayout!=null) addView(mWaitingToLoadLayout/*, params*/);

        switchLayout(POS_LOADING);
    }

    /**
     * switch layout visibility to loading/error
     * @param state loading {@link #POS_LOADING} or error {@link #POS_ERROR}
     */
    public void switchLayout(@IntRange(from=POS_ERROR,to=POS_WAITING) int state){
        if(POS_ERROR<getChildCount()) getChildAt(POS_ERROR).setVisibility(state==POS_ERROR?VISIBLE:GONE);
        if(POS_LOADING<getChildCount()) getChildAt(POS_LOADING).setVisibility(state==POS_LOADING?VISIBLE:GONE);
        if(POS_WAITING<getChildCount())  getChildAt(POS_WAITING).setVisibility(state==POS_WAITING?VISIBLE:GONE);
    }

    /**
     * set listener for click event on button in error layout (if available) and handle exceptions
     * @param clickListener
     */
    public void setOnErrorClickListener(OnClickListener clickListener){
        try{
            trySetOnErrorClickListener(clickListener);
        }catch (Resources.NotFoundException nfe){
            nfe.printStackTrace();
        }
    }

    /**
     * try setting listener for click event on button in error layout
     * @param clickListener
     * @throws Resources.NotFoundException if there's no action view to bind the listener
     */
    private void trySetOnErrorClickListener(OnClickListener clickListener) throws Resources.NotFoundException{
        if(mErrorActionViewIdRes !=RES_ID_NULL) {
            getChildAt(POS_ERROR).findViewById(mErrorActionViewIdRes).setOnClickListener(clickListener);
        }else {
            throw new Resources.NotFoundException("Have you declared an error action view on your layout?");
        }
    }

    /**
     * set listener for click event on action view in waitingToLoad layout (if available) and handle exceptions
     * @param clickListener
     */
    public void setOnWaitingToLoadClickListener(OnClickListener clickListener){
        try{
            trySetOnWaitingToLoadClickListener(clickListener);
        }catch (Resources.NotFoundException nfe){
            nfe.printStackTrace();
        }
    }

    /**
     * try setting listener for click event on action view in waitingToLoad layout
     * @param clickListener
     * @throws Resources.NotFoundException if there's no action view to bind the listener
     */
    private void trySetOnWaitingToLoadClickListener(OnClickListener clickListener) throws Resources.NotFoundException{
        if(mWaitingToLoadActionViewIdRes !=RES_ID_NULL) {
            getChildAt(POS_WAITING).findViewById(mWaitingToLoadActionViewIdRes).setOnClickListener(clickListener);
        }else {
            throw new Resources.NotFoundException("Have you declared a waitingToLoad action view on your layout?");
        }
    }

    public View getLoadingLayout() {
        return mLoadingLayout;
    }

    public View getErrorLayout() {
        return mErrorLayout;
    }

    public View getWaitingToLoadLayout() {
        return mWaitingToLoadLayout;
    }
}
