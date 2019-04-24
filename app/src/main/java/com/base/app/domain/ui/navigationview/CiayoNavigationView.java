package com.base.app.domain.ui.navigationview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.base.app.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by ferrytan on 05/02/18.
 */

public class CiayoNavigationView extends LinearLayout {
    public static final String KEY_MENU = "menu";
    public static final int DEFAULT_ACTIVE_TAB_ID = LottieMenu.MENU_MEMBER;
    private List<LottieMenu> mMenus;
    private LayoutParams mLayoutParams;
    private OnNavigationItemSelectedListener mListener;
    private LottieAnimationView mActiveView;

    public CiayoNavigationView(Context context) {
        super(context);
        init();
    }

    public CiayoNavigationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CiayoNavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_indicator_white));
        mLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutParams.weight = 1;

        mMenus = createMenusFromRawAsset();

        for (LottieMenu menu : mMenus) {
            addMenu(menu);
        }

        requestLayout();
    }

    public List<LottieMenu> createMenusFromRawAsset() {
        JsonParser jsonParser = new JsonParser();
        JsonElement menuJson = jsonParser.parse(inputStreamToString(getResources().openRawResource(R.raw.menu)));
        Type listType = new TypeToken<List<LottieMenu>>() {
        }.getType();
        return new Gson().fromJson(menuJson.getAsJsonObject().get(KEY_MENU).getAsJsonArray(), listType);
    }

    public String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            return new String(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    public void addMenu(final LottieMenu menu) {
        final LottieAnimationView lottieAnimationView = new LottieAnimationView(getContext());
        lottieAnimationView.setTag(menu.getId());
        lottieAnimationView.setAnimation(menu.getLottieAsset(), LottieAnimationView.CacheStrategy.Strong);
        lottieAnimationView.setRepeatCount(0);
        lottieAnimationView.setRepeatMode(LottieDrawable.RESTART);
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);
        lottieAnimationView.setSpeed(1.5f);

        lottieAnimationView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onNavigationItemSelected(menu.getId());
            }

            if (mActiveView != null) {
                if (mActiveView.getTag() == lottieAnimationView.getTag()) return;
                mActiveView.cancelAnimation();
                mActiveView.setFrame(menu.getInactiveStillIndex());
            }

            setActiveView(lottieAnimationView);
        });

        if (menu.getId() == DEFAULT_ACTIVE_TAB_ID) {
            setActiveView(lottieAnimationView);

        }
        addView(lottieAnimationView, mLayoutParams);
    }

    public void setActiveView(LottieAnimationView lottieAnimationView) {
        mActiveView = lottieAnimationView;
        lottieAnimationView.playAnimation();
    }

    public void setOnMenuSelectedListener(OnNavigationItemSelectedListener menuSelectedListener) {
        this.mListener = menuSelectedListener;
    }

    public interface OnNavigationItemSelectedListener {
        void onNavigationItemSelected(int menuId);
    }

    public void switchMenu(int menuId) {
        View menuView = findViewWithTag(menuId);
        if (menuView != null)
            menuView.performClick();
    }
}
