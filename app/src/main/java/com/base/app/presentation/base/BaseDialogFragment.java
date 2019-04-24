package com.base.app.presentation.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.app.R;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseDialogFragment<P extends BasePresenter> extends DialogFragment {

    private P presenter;
    public final int ANIM_RIGHT_TO_LEFT = 0;
    public final int ANIM_LEFT_TO_RIGHT = 1;
    public final int ANIM_DOWN_TO_TOP = 2;
    public final int ANIM_TOP_TO_DOWN = 3;
    public final int ANIM_DOWN_TO_TOP_BOUNCE = 4;

    private View view;

    public abstract P createPresenter();
    public void setPresenter() { presenter = createPresenter(); }
    public P getPresenter() { return presenter; }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Load layout
        view = onCreateLayout(inflater, container, savedInstanceState);

        // Bind ButterKnife
        ButterKnife.bind(this, view);

        if (this instanceof BaseViewImpl) {
            setPresenter();

            if (getPresenter() != null) {
                getPresenter().attachView((BaseViewImpl) this);
            }
        }

        // Call init action
        initAction(view);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getPresenter() != null) {
            getPresenter().detachView();
        }
    }

    public abstract View onCreateLayout(LayoutInflater inflater, @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState);
    public abstract void initAction(View view);


    public void startActivityWithAnim(Context ctx, Class destination, int animType, boolean isFinish, Bundle bundle) {
        if (isFinish) {
            ((Activity) ctx).finish();
        }
        startActivity(bundle != null ? new Intent(ctx, destination).putExtras(bundle) : new Intent(ctx, destination));
        doActivityAnim(animType, false);
    }

    public void startActivityResultWithAnim(Context ctx, Class destination, int reqCode, int animType, boolean isFinish, Bundle bundle) {
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

    private void doActivityAnim(int animType, boolean finish){
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

    public void showSnackbar(Context context, View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setAction("Action", null);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_600));
        snackbar.show();

    }

}
