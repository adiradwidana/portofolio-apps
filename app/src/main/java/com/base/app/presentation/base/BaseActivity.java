package com.base.app.presentation.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.base.app.R;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    // Animation Stuff
    public final int ANIM_RIGHT_TO_LEFT = 0;
    public final int ANIM_LEFT_TO_RIGHT = 1;
    public final int ANIM_DOWN_TO_TOP = 2;
    public final int ANIM_TOP_TO_DOWN = 3;
    public final int ANIM_DOWN_TO_TOP_BOUNCE = 4;
    public MenuItem menuItem;
    private P presenter;

    public abstract P createPresenter();

    public void setPresenter() {
        presenter = createPresenter();
    }

    public P getPresenter() {
        return presenter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view, create layout implementation
        setContentView(createLayout());

        // Bind ButterKnife
        ButterKnife.bind(this);

        if (this instanceof BaseViewImpl) {
            setPresenter();
            if (getPresenter() != null) {
                getPresenter().attachView((BaseViewImpl) this);
            }
        }

        // init action
        startingUpActivity(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (getPresenter() != null) {
            getPresenter().detachView();
        }
    }

    public abstract int createLayout();

    public abstract void startingUpActivity(Bundle savedInstanceState);


    public void startActivityWithAnim(Context ctx, Class destination, int animType, boolean isFinish, Bundle bundle) {
        if (isFinish) {
            ((Activity) ctx).finish();
        }
        startActivity(bundle != null ? new Intent(ctx, destination).putExtras(bundle) : new Intent(ctx, destination));
        doActivityAnim(ctx, animType, false);
    }

    public void startActivityResultWithAnim(Context ctx, Class destination, int reqCode, int animType, boolean isFinish, Bundle bundle) {
        if (isFinish) {
            ((Activity) ctx).finish();
        }
        startActivityForResult(bundle != null ? new Intent(ctx, destination).putExtras(bundle)
                : new Intent(ctx, destination), reqCode);
        doActivityAnim(ctx, animType, false);
    }

    public void finishWithAnim(Context context, int animType) {
        finish();
        doActivityAnim(context, animType, true);
    }

    private void doActivityAnim(Context context, int animType, boolean finish) {
        switch (animType) {
            case ANIM_RIGHT_TO_LEFT:
                ((Activity) context).overridePendingTransition(R.anim.anim_come_right_to_left, R.anim.anim_going_right_to_left);
                break;
            case ANIM_LEFT_TO_RIGHT:
                ((Activity) context).overridePendingTransition(R.anim.anim_come_left_to_right, R.anim.anim_going_left_to_right);
                break;
            case ANIM_DOWN_TO_TOP:
                if (finish) {
                    ((Activity) context).overridePendingTransition(R.anim.anim_come_down_to_top, R.anim.anim_going_top_to_down);
                } else {
                    ((Activity) context).overridePendingTransition(R.anim.anim_come_down_to_top, R.anim.anim_idle);
                }
                break;
            case ANIM_TOP_TO_DOWN:
                ((Activity) context).overridePendingTransition(R.anim.anim_idle, R.anim.anim_going_top_to_down);
                break;
            case ANIM_DOWN_TO_TOP_BOUNCE:
                if (finish) {
                    ((Activity) context).overridePendingTransition(R.anim.anim_idle, R.anim.anim_going_top_to_down_bounce);
                } else {
                    ((Activity) context).overridePendingTransition(R.anim.anim_come_down_to_top_bounce, R.anim.anim_idle);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
        }

        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void showToast(String messeage, Context context){
        Toast toast = Toast.makeText(context, messeage, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        //Gets the TextView from the Toast so it can be editted
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(getResources().getColor(R.color.white));


        toast.show();
    }
}
