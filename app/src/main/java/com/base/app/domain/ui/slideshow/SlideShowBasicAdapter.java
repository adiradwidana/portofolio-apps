package com.base.app.domain.ui.slideshow;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.base.app.R;
import com.base.app.domain.ui.imageview.RatioImageView;
import com.base.app.domain.utils.graphic.DimensionUtil;

import java.util.List;


/**
 * Created by ganjarramadhan on 11/1/16.
 */

public class SlideShowBasicAdapter extends PagerAdapter {

    private Context mContext;
    private List<SlideItem> mSlideShowItemList;
    private LayoutInflater mLayoutInflater;
    private SlideShowListener mListener;

    public SlideShowBasicAdapter(Context mContext, List<SlideItem> mSlideShowItemList, SlideShowListener mListener) {
        this.mContext = mContext;
        this.mSlideShowItemList = mSlideShowItemList;
        this.mListener = mListener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mSlideShowItemList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mLayoutInflater.inflate(R.layout.slide_show_item, container, false);

        final RatioImageView imageView = view.findViewById(R.id.slide_show_item_image);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)imageView.getLayoutParams();
        lp.width = DimensionUtil.getDisplayWidth(mContext);
        lp.height = (int)((double)lp.width/1.5);
        imageView.setLayoutParams(lp);

        Glide.with(mContext)
                .load(mSlideShowItemList.get(position).getUrlImage())
                .into(imageView);

        container.addView(view);
        view.setOnClickListener(v -> mListener.onSlideShowItemClicked(mSlideShowItemList.get(position).getUrlContent()));
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public interface SlideShowListener{
        void onSlideShowItemClicked(String item);
    }
}
