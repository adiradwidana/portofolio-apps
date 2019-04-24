package com.base.app.presentation.webview;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.base.app.R;
import com.base.app.domain.ui.PongodevStateView;
import com.base.app.domain.utils.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebviewFragment extends Fragment {
    private static final String TAG = "WebviewFragment";
    public static final String ARG_URL = "url";

    @BindView(R.id.progressbar) ProgressBar mProgressbar;
    @BindView(R.id.webview) WebView mWebView;
    @BindView(R.id.content_webview_root) RelativeLayout mContentWebviewRoot;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.frm_root) FrameLayout frameRoot;

    private String mUrl;
    private com.base.app.domain.ui.PongodevStateView PongodevStateView;
    private boolean isAlreadyLoad, delayLoad;


    public WebviewFragment() {

    }

    public static WebviewFragment newInstance(String url) {
        Logger.d("Pongodev-LOG", "WebviewFragment:newInstance -> " + url);

        WebviewFragment fragment = new WebviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);
        }
        Logger.d("Pongodev-LOG", "WebviewFragment:onCreatex -> ");

        isAlreadyLoad = false;
        delayLoad = false;

        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                if (getView() == null) {
                    h.post(this);
                } else {
                    loadWebview(mUrl);
                    isAlreadyLoad = true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_webview, container, false);
        ButterKnife.bind(this, rootView);
        PongodevStateView = PongodevStateView.inject(frameRoot);
        PongodevStateView.setOnRetryClickListener(new PongodevStateView.OnErrorActionClickListener() {
            @Override
            public void onRetryClick() {
                PongodevStateView.showLoading();
                loadWebview(mUrl);
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PongodevStateView.showLoading();
                mSwipeRefresh.setRefreshing(false);
                loadWebview(mUrl);

            }
        });
        return rootView;
    }

    // Method to load url in WebView
    private void loadWebview(String url) {
        // Enable Javascript
//        WebSettings webSettings = mWebView.getSettings();
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //Only hide the scrollbar, not disables the scrolling:
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);

        //Only disabled the horizontal scrolling:
//        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        // Load url in WebView
        mWebView.loadUrl(url);

        // No Horizontal scroll but cant clicked
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            float m_downX;

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        m_downX = event.getX();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.getY());
                        mWebView.performClick();
                        break;
                    }

                }
                return false;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView webview, int progress) {
                mProgressbar.setProgress(progress);
            }

        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(mWebView, url, favicon);
                // On page start display ProgressBar and WebView
                PongodevStateView.showLoading();
                mProgressbar.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                Logger.d(TAG, "onPageStarted() called with: view = [" + view + "], url = [" + url + "], favicon = [" + favicon + "]");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(mWebView, url);
                // When finish loading page, set progress bar to 0 and hide it
                // Display WebView
                mProgressbar.setProgress(0);
                mProgressbar.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
                Logger.d(TAG, "onPageFinished() called with: view = [" + view + "], url = [" + url + "]");
                PongodevStateView.showContent();
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {

                view.stopLoading();
                if (errorCode == ERROR_HOST_LOOKUP)
                    PongodevStateView.showError(PongodevStateView.ERROR_CONNECTION);
                else if (errorCode == ERROR_TIMEOUT)
                    PongodevStateView.showError(PongodevStateView.ERROR_TIMEOUT);
                else
                    PongodevStateView.showError(PongodevStateView.ERROR_UNKNOWN);
                Logger.d(TAG, "onReceivedError() called with: view = [" + view + "], errorCode = [" + errorCode + "], description = [" + description + "], failingUrl = [" + failingUrl + "]");
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(),
                        req.getUrl().toString());
            }

        });
    }

    /*@Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        Logger.d(TAG, "setMenuVisibility() called with: menuVisible = [" + menuVisible + "]");
        if (menuVisible && !isAlreadyLoad) {
            final Handler h = new Handler();
            h.post(new Runnable() {
                @Override
                public void run() {
                    if(getView() == null){
                        h.post(this);
                    }else {
                        loadWebview(mUrl);
                        isAlreadyLoad = true;
                    }
                }
            });
        }
    }*/
}