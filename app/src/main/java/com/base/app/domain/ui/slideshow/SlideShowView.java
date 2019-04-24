package com.base.app.domain.ui.slideshow;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.base.app.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ganjarramadhan on 11/1/16.
 */

public class SlideShowView extends RelativeLayout {

    LayoutInflater mLayoutInflater;
    Context mContext;
    LoopViewPager mViewPager;
    CircleIndicator mCircleIndicator;
    View mView;

    int currentPage;
    boolean paused = false;
    boolean hasStarted = false;
    Timer swipeTimer;

    public SlideShowView(Context context) {
        super(context);
        init(context);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlideShowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mView = mLayoutInflater.inflate(R.layout.slide_show, this, true);
        mViewPager = (LoopViewPager) mView.findViewById(R.id.slide_show_pager);
        mCircleIndicator = (CircleIndicator) mView.findViewById(R.id.slide_show_indicator);
        swipeTimer = new Timer();
    }

    @SuppressWarnings("unchecked")
    public void start(final List slideShowItemList, SlideShowBasicAdapter.SlideShowListener listener){
        final SlideShowBasicAdapter slideShowBasicAdapter = new SlideShowBasicAdapter(mContext, slideShowItemList, listener);
        mViewPager.setAdapter(slideShowBasicAdapter);
        mCircleIndicator.setViewPager(mViewPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == slideShowItemList.size() + 1) {
                    currentPage = 0;
                    mViewPager.setCurrentItem(currentPage, true);
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };

        if (!hasStarted) {
            swipeTimer.cancel();
            swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    hasStarted = true;
                    if (!paused)
                        handler.post(Update);
                }
            }, 0, 3000);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                CiayoLog.d("ViewPager", "OnPageScrolledPositionOffset: " + positionOffset + ". OffsetPixel: " + positionOffsetPixels);
                if (positionOffsetPixels == 0 && position == currentPage) {
                    paused = false;
                    swipeTimer.cancel();
                    swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!paused)
                                handler.post(Update);
                        }
                    }, 3000, 3000);
                }
            }

            @Override
            public void onPageSelected(int position) {
//                CiayoLog.d("ViewPager", "OnPageSelectedPosition: " + position);
                paused = false;
                swipeTimer.cancel();
                swipeTimer = new Timer();
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!paused)
                            handler.post(Update);
                    }
                }, 3000, 3000);
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                CiayoLog.d("ViewPager", "OnPageScrollStateChanged: " + state);
                if (state == ViewPager.SCROLL_STATE_DRAGGING){
                    paused = true;
                    swipeTimer.cancel();
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width / 1.5);
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
            }
            setMeasuredDimension(width, height);
            mView.getLayoutParams().height = height;
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
