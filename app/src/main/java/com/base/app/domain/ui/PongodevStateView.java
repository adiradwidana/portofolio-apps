package com.base.app.domain.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.app.R;


/**
 * Created by C on 4/3/2016.
 * https://github.com/nukc
 * Modified by cahaya on 11/29/2016
 * re-modified to match Ciayo Comics project by cahaya on 12/05/2016
 */

/**
 * HOW TO USE
 * - Inject StateView
 * - Set on retry click listener
 * - PongodevStateView has 3 state: LoadingState, and Error State
 *   You can access one of error state via static constant in PongodevStateView
 */
public class PongodevStateView extends View {

    public static final int ERROR_TIMEOUT = 0;
    public static final int ERROR_CONNECTION = 1;
    public static final int ERROR_EMPTY_DATA = 2;
    public static final int ERROR_SERVER_MAINTENANCE = 3;
    public static final int ERROR_NOT_FOUND = 4;
    public static final int ERROR_NEED_UPDATE = 5;
    public static final int ERROR_UNKNOWN = 6;
    public static final int ERROR_NOT_LOGIN = 7;
    public static final int ERROR_EMPTY_SUBSCRIPTION = 8;
    public static final int ERROR_INVALID_TOKEN = 9;
    public static final int ERROR_UNDER_CONSTRACTIONS = 10;
    public static final int ERROR_NOTIFICATION_EMPTY = 11;
    public static final int ERROR_UNDEFINED = 12;

    private int mErrorResource;
    private int mLoadingResource;

    private View mErrorView;
    private View mLoadingView;

    private LayoutInflater mInflater;
    private OnErrorActionClickListener mOnErrorActionClickListener;

    private LinearLayout.LayoutParams mLayoutParams;


    /**
     * 注入到activity中
     *
     * @param activity Activity
     * @return StateView
     */
    public static PongodevStateView inject(@NonNull Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        return inject(rootView);
    }

    /**
     * 注入到activity中
     *
     * @param activity Activity
     * @param hasActionBar 是否有actionbar/toolbar,
     *                     true: 会setMargin top, margin大小是状态栏高度 + 工具栏高度
     *                     false: not set
     * @return StateView
     */
    @Deprecated
    public static PongodevStateView inject(@NonNull Activity activity, boolean hasActionBar) {
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
        return inject(rootView, hasActionBar, true);
    }

    /**
     * 注入到ViewGroup中
     *
     * @param parent extends ViewGroup
     * @return StateView
     */
    public static PongodevStateView inject(@NonNull ViewGroup parent) {
        return inject(parent, false);
    }

    /**
     * 注入到ViewGroup中
     *
     * @param parent extends ViewGroup
     * @param hasActionBar 是否有actionbar/toolbar,
     *                     true: 会setMargin top, margin大小是actionbarSize
     *                     false: not set
     * @return StateView
     */
    public static PongodevStateView inject(@NonNull ViewGroup parent, boolean hasActionBar) {
        PongodevStateView pongodevStateView = new PongodevStateView(parent.getContext());
        parent.addView(pongodevStateView);
        if (hasActionBar) {
            pongodevStateView.setTopMargin();
        }
        return pongodevStateView;
    }

    /**
     * 注入到ViewGroup中
     *
     * @param parent extends ViewGroup
     * @param hasActionBar 是否有actionbar/toolbar
     * @param isActivity 是否注入到Activity
     * @return StateView
     */
    @Deprecated
    private static PongodevStateView inject(@NonNull ViewGroup parent, boolean hasActionBar, boolean isActivity) {
        PongodevStateView pongodevStateView = new PongodevStateView(parent.getContext());
        parent.addView(pongodevStateView);
        if (hasActionBar) {
            pongodevStateView.setTopMargin();
        }
        return pongodevStateView;
    }

    /**
     * 注入到View中
     *
     * @param view instanceof ViewGroup
     * @return StateView
     */
    public static PongodevStateView inject(@NonNull View view) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            return inject(parent);
        } else {
            throw new ClassCastException("view must be ViewGroup");
        }
    }

    /**
     * 注入到View中
     *
     * @param view instanceof ViewGroup
     * @param hasActionBar 是否有actionbar/toolbar
     * @return StateView
     */
    public static PongodevStateView inject(@NonNull View view, boolean hasActionBar) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            return inject(parent, hasActionBar);
        } else {
            throw new ClassCastException("view must be ViewGroup");
        }
    }

    public PongodevStateView(Context context) {
        this(context, null);
    }

    public PongodevStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PongodevStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PongodevStateView);
        mErrorResource = a.getResourceId(R.styleable.PongodevStateView_emptyResource, 0);
        mLoadingResource = a.getResourceId(R.styleable.PongodevStateView_loadingResource, 0);
        a.recycle();

        if (mErrorResource == 0){
            mErrorResource = R.layout.state_layout;
        }
        if (mLoadingResource == 0) {
            mLoadingResource = R.layout.state_loading;
        }

        if (attrs == null) {
            mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            mLayoutParams = new LinearLayout.LayoutParams(context, attrs);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(0, 0);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
    }

    @Override
    public void setVisibility(int visibility) {
        setVisibility(mErrorView, visibility);
        setVisibility(mLoadingView, visibility);
    }

    private void setVisibility(View view, int visibility){
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    public void showContent(){
        setVisibility(GONE);
    }

    public View showError(int type){
        if (mErrorView == null) {
            mErrorView = inflate(mErrorResource);
        }
        populateView(mErrorView, type, null, null);
        showView(mErrorView);
        return mErrorView;
    }

    public View showLoading(){
        if (mLoadingView == null) {
            mLoadingView = inflate(mLoadingResource);
        }

        showView(mLoadingView);
        return mLoadingView;
    }

    private void showView(View view){
        setVisibility(view, VISIBLE);
        if (mErrorView == view){
            setVisibility(mLoadingView, GONE);
        }else if (mLoadingView == view){
            setVisibility(mErrorView, GONE);
        }else {
            setVisibility(mErrorView, GONE);
            setVisibility(mLoadingView, GONE);
        }
    }

    public View inflate(@LayoutRes int layoutResource) {
        final ViewParent viewParent = getParent();

        if (viewParent != null && viewParent instanceof ViewGroup) {
            if (layoutResource != 0) {
                final ViewGroup parent = (ViewGroup) viewParent;
                final LayoutInflater factory;
                if (mInflater != null) {
                    factory = mInflater;
                } else {
                    factory = LayoutInflater.from(getContext());
                }
                final View view = factory.inflate(layoutResource, parent, false);

                final int index = parent.indexOfChild(this);
                //防止还能触摸底下的View
                view.setClickable(true);

                final ViewGroup.LayoutParams layoutParams = getLayoutParams();
                if (layoutParams != null) {
                    if (parent instanceof RelativeLayout) {
                        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) layoutParams;
                        mLayoutParams.setMargins(lp.leftMargin, lp.topMargin,
                                lp.rightMargin, lp.bottomMargin);

                        parent.addView(view, index, mLayoutParams);
                    }else {
                        parent.addView(view, index, layoutParams);
                    }
                } else {
                    parent.addView(view, index);
                }

                if (mLoadingView != null && mErrorView != null){
                    parent.removeViewInLayout(this);
                }

                return view;
            } else {
                throw new IllegalArgumentException("StateView must have a valid layoutResource");
            }
        } else {
            throw new IllegalStateException("StateView must have a non-null ViewGroup viewParent");
        }
    }

    /**
     * 设置topMargin, 当有actionbar/toolbar的时候
     * @param isActivity if true: 注入到Activity, 需要加上状态栏的高度
     */
    @Deprecated
    public void setTopMargin(boolean isActivity){
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        layoutParams.topMargin = getActionBarHeight();
    }

    public void setTopMargin() {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        layoutParams.topMargin = getActionBarHeight();
    }

    /**
     * @return 状态栏的高度
     */
    @Deprecated
    private int getStatusBarHeight() {
        int height = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            height = getResources().getDimensionPixelSize(resId);
        }
        return height;
    }

    /**
     * @return actionBarSize
     */
    private int getActionBarHeight() {
        int height = 0;
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return height;
    }

    /**
     * 设置emptyView的自定义Layout
     * @param emptyResource emptyView的layoutResource
     */
    public void setEmptyResource(@LayoutRes int emptyResource) {
        this.mErrorResource = emptyResource;
    }

    /**
     * 设置loadingView的自定义Layout
     * @param loadingResource loadingView的layoutResource
     */
    public void setLoadingResource(@LayoutRes int loadingResource) {
        mLoadingResource = loadingResource;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.mInflater = inflater;
    }

    /**
     * 监听重试
     * @param listener {@link OnErrorActionClickListener}
     */
    public void setOnRetryClickListener(OnErrorActionClickListener listener){
        this.mOnErrorActionClickListener = listener;
    }

    public interface OnErrorActionClickListener {
        void onRetryClick();
    }

    private void populateView(View view, int type, String customTitle, String customMessage){
        ImageView imgIcon = (ImageView) view.findViewById(R.id.state_layout_img_icon);
        TextView txtTitle = (TextView) view.findViewById(R.id.state_layout_txt_title);
        TextView txtMessage = (TextView) view.findViewById(R.id.state_layout_txt_message);
        Button btnRetry = (Button) view.findViewById(R.id.state_layout_btn_reload);

        @StringRes int titleStringRes, messageStringRes;

        switch (type) {
            case ERROR_CONNECTION:
                btnRetry.setText(R.string.reload);
                titleStringRes = R.string.error_no_connection_title;
                messageStringRes = R.string.error_no_connection_message;
                break;
            case ERROR_EMPTY_DATA:
                titleStringRes = R.string.error_no_data;
                messageStringRes = R.string.error_no_data_message;
                break;
            case ERROR_NOTIFICATION_EMPTY:
                titleStringRes = R.string.error_no_data;
                messageStringRes = R.string.error_no_notification;
                break;
            case ERROR_TIMEOUT:
                titleStringRes = R.string.error_no_connection_title;
                messageStringRes = R.string.error_no_connection_message;
                break;
            case ERROR_SERVER_MAINTENANCE:
                titleStringRes = R.string.error_no_connection_title;
                messageStringRes = R.string.error_no_connection_message;
                break;
            case ERROR_NOT_FOUND:
                titleStringRes = R.string.error_no_connection_title;
                messageStringRes = R.string.error_no_connection_message;
                break;
            case ERROR_NEED_UPDATE:
                titleStringRes = R.string.error_no_connection_title;
                messageStringRes = R.string.error_no_connection_message;
                break;
            case ERROR_NOT_LOGIN:
                titleStringRes = R.string.error_no_connection_title;
                messageStringRes = R.string.error_no_connection_message;
                break;
            case ERROR_EMPTY_SUBSCRIPTION:
                titleStringRes = R.string.error_no_connection_title;
                messageStringRes = R.string.error_no_connection_message;
                break;
            case ERROR_INVALID_TOKEN:
                titleStringRes = R.string.error_no_connection_title;
                messageStringRes = R.string.error_no_connection_message;
                break;
            case ERROR_UNDER_CONSTRACTIONS:
                titleStringRes = R.string.error_under_constraction_title;
                messageStringRes = R.string.error_under_constraction_message;
                break;
            default:
                type = ERROR_UNDEFINED;
                titleStringRes = R.string.error_unknown_title;
                messageStringRes = R.string.error_unknown_message;
                break;
        }

        String title = customTitle==null?getResources().getString(titleStringRes):customTitle;
        String message = customMessage==null?getResources().getString(messageStringRes):customMessage;
        txtTitle.setText(title);
        txtMessage.setText(message);

        boolean buttonShown = type== ERROR_CONNECTION || type == ERROR_TIMEOUT || type == ERROR_NEED_UPDATE ||
                type == ERROR_NOT_LOGIN || type == ERROR_UNDEFINED;
        btnRetry.setVisibility(buttonShown?VISIBLE:GONE);
        btnRetry.setOnClickListener(buttonShown?new OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                if(mOnErrorActionClickListener !=null)
                    mOnErrorActionClickListener.onRetryClick();
            }
        }:null);
    }
}
