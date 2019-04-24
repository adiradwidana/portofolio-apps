package com.base.app.presentation.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.EditText;

import com.base.app.R;
import com.base.app.presentation.base.BaseActivity;
import com.base.app.presentation.home.main.MainActivity;
import com.base.app.domain.utils.Validation;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.edtEmail) EditText edtEmail;
    @BindView(R.id.edtPassword) EditText edtPassword;

    private ProgressDialog mProgressDialog;
    private boolean isPassVisisble;
    private String mEmail;
    private String mPassword;

    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public int createLayout() {
        return R.layout.activity_login;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void startingUpActivity(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Set password hint font so that it looks like default font
        edtPassword.setTypeface(Typeface.DEFAULT);
        edtPassword.setTransformationMethod(new PasswordTransformationMethod());
        edtPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    if (isPassVisisble)
                        edtPassword.setTransformationMethod(null);
                    else
                        edtPassword.setTransformationMethod(new PasswordTransformationMethod());

                    edtPassword.setSelection(edtPassword.getText().length());

                    edtPassword.setSelected(isPassVisisble);
                    isPassVisisble = !isPassVisisble;

                    return true;
                }
            }
            return false;
        });
        edtEmail.setText("faizf66@gmail.com");
        edtPassword.setText("poinmas123");
    }


    @Override
    public void displayLoginFailed(int stateCode, String message) {
        displayResult();
        showToast("displayLoginFailed: " + message, this);
    }

    @Override
    public void displayLoginSuccess() {
        displayResult();
        showToast(getString(R.string.login_success),this);
        startActivityWithAnim(this, MainActivity.class, ANIM_RIGHT_TO_LEFT, true, null);
    }

    private void displayResult() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    @OnClick(R.id.btnLogin)
    void setBtnLogin() {
        mEmail = edtEmail.getText().toString().trim();
        mPassword = edtPassword.getText().toString().trim();
        boolean isValidUsername = Validation.checkEmail(edtEmail);
        boolean isValidPassword = Validation.checkPassword(edtPassword);
        if (isValidUsername && isValidPassword) {
            getPresenter().loadDataFromLogin(mEmail, mPassword);
        }
    }
}