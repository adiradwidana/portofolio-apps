package com.base.app.domain.ui.navigationview;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ferrytan on 05/02/18.
 */

public class LottieMenu {
    public static final int MENU_MEMBER = 0;
    public static final int MENU_OTLET_LOCATION = 1;
    public static final int MENU_TRACKER = 2;
    public static final int MENU_SHOPPING = 3;
    @SerializedName("id")
    private int mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("lottie_asset")
    private String mLottieAsset;
    @SerializedName("active_animation_start_index")
    private int mActiveStartIndex;
    @SerializedName("inactive_still_index")
    private int mInactiveStillIndex;

    public LottieMenu() {
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLottieAsset() {
        return mLottieAsset;
    }

    public void setLottieAsset(String lottieAsset) {
        mLottieAsset = lottieAsset;
    }

    public int getActiveStartIndex() {
        return mActiveStartIndex;
    }

    public void setActiveStartIndex(int activeStartIndex) {
        mActiveStartIndex = activeStartIndex;
    }

    public int getInactiveStillIndex() {
        return mInactiveStillIndex;
    }

    public void setInactiveStillIndex(int inactiveStillIndex) {
        mInactiveStillIndex = inactiveStillIndex;
    }
}