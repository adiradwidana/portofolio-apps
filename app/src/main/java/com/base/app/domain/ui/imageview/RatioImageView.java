package com.base.app.domain.ui.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.base.app.R;


/**
 * Created by cahaya on 6/30/17.
 */

public class RatioImageView extends AppCompatImageView {
    public static final int DEFAULT_RATIO = 1;
    private int widthRatio, heightRatio;

    public RatioImageView(Context context) {
        super(context);
        init(context, null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void setWidthRatio(int widthRatio) {
        this.widthRatio = widthRatio;
    }

    public void setHeightRatio(int heightRatio) {
        this.heightRatio = heightRatio;
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.RatioImageView);

        widthRatio = a.getInt(R.styleable.RatioImageView_widthRatio, DEFAULT_RATIO);
        heightRatio = a.getInt(R.styleable.RatioImageView_heightRatio, DEFAULT_RATIO);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            double height = (double) width / ((double) widthRatio / (double) heightRatio);
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
            }
            setMeasuredDimension(width, (int) height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
