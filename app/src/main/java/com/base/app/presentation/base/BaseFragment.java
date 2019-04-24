package com.base.app.presentation.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.app.R;

import butterknife.ButterKnife;

public abstract class BaseFragment<P extends BasePresenter>  extends Fragment {

    private P mPresenter;
    public static final int ANIM_RIGHT_TO_LEFT = 0;
    public static final int ANIM_LEFT_TO_RIGHT = 1;
    public static final int ANIM_DOWN_TO_TOP = 2;
    public static final int ANIM_TOP_TO_DOWN = 3;
    public static final int ANIM_DOWN_TO_TOP_BOUNCE = 4;

    protected abstract P createPresenter();

    private void setPresenter() {
        mPresenter = createPresenter();
    }

    protected P getPresenter() {
        return mPresenter;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // load layout
        View view = onCreateLayout(inflater, container, savedInstanceState);

        // inject butter knife to init view
        ButterKnife.bind(this, view);

        if (this instanceof BaseViewImpl) {
            setPresenter();

            if (getPresenter() != null)
                getPresenter().attachView((BaseViewImpl) this);
        }
        // call init action
        initAction(view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getPresenter() != null)
            getPresenter().detachView();
    }

    protected abstract View onCreateLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected abstract void initAction(View view);

    protected void startActivityWithAnim(Context ctx, Class destination, int animType, boolean isFinish, Bundle bundle) {
        if (isFinish) {
            ((Activity) ctx).finish();
        }
        startActivity(bundle != null ? new Intent(ctx, destination).putExtras(bundle) : new Intent(ctx, destination));
        doActivityAnim(animType, false);
    }

    protected void startActivityResultWithAnim(Context ctx, Class destination, int reqCode, int animType, boolean isFinish, Bundle bundle) {
        if (isFinish) {
            ((Activity) ctx).finish();
        }
        startActivityForResult(bundle != null ? new Intent(ctx, destination).putExtras(bundle)
                : new Intent(ctx, destination), reqCode);
        doActivityAnim(animType, false);
    }

    public void finishWithAnim(int animType) {
        getActivity().finish();
        doActivityAnim(animType, true);
    }

    private void doActivityAnim(int animType, boolean finish) {
        switch (animType) {
            case ANIM_RIGHT_TO_LEFT:
                getActivity().overridePendingTransition(R.anim.anim_come_right_to_left, R.anim.anim_going_right_to_left);
                break;
            case ANIM_LEFT_TO_RIGHT:
                getActivity().overridePendingTransition(R.anim.anim_come_left_to_right, R.anim.anim_going_left_to_right);
                break;
            case ANIM_DOWN_TO_TOP:
                if (finish) {
                    getActivity().overridePendingTransition(R.anim.anim_come_down_to_top, R.anim.anim_going_top_to_down);
                } else {
                    getActivity().overridePendingTransition(R.anim.anim_come_down_to_top, R.anim.anim_idle);
                }
                break;
            case ANIM_TOP_TO_DOWN:
                getActivity().overridePendingTransition(R.anim.anim_idle, R.anim.anim_going_top_to_down);
                break;
            case ANIM_DOWN_TO_TOP_BOUNCE:
                if (finish) {
                    getActivity().overridePendingTransition(R.anim.anim_idle, R.anim.anim_going_top_to_down_bounce);
                } else {
                    getActivity().overridePendingTransition(R.anim.anim_come_down_to_top_bounce, R.anim.anim_idle);
                }
                break;
            default:
                break;
        }
    }
}