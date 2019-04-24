package com.base.app.domain.restconnection.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.base.app.R;
import com.base.app.domain.restconnection.callback.CallbackConnection;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by faizf on 2/25/2017.
 */
public class Utils {

    protected Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public String VALIDATION_MAX24 = "Masukkan minimal 3 karakter";
    public String VALIDATION_MINMAX13 = "Masukkan minimal 10 karakter";

    public static String noSpecialChar = "^[\\w_\\s]+$";
    public static String address = "^[\\w\\s.]+$";
    public static String usernameRegex = "^[\\w_]+$";
    public static String inputForm = "^[\\w,\\s]+$";
    public static String onlyCharUnderScore = "^[a-zA-Z\\s]+$";
    public static String onlyDigits = "^\\d+$";

    static final Integer LOCATION = 0x1;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public <T> ArrayList<T> dummydata(Class<T> tClass) {
        ArrayList<T> models = new ArrayList<>();
        T model;
        try {
            model = tClass.newInstance();
            for (int i = 0; i < 10; i++) {
                models.add(model);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return models;
    }

    public Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    // custom toast
    public void showToast(String message) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) ((Activity) context).findViewById(R.id.rootToast));
        ((TextView) layout.findViewById(R.id.textToast)).setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static void snackBar(CoordinatorLayout view, Context context) {
        Snackbar.make(view, "Make sure your device has an internet connection", Snackbar.LENGTH_LONG).show();
    }

    public static void snackBarWithText(String message, CoordinatorLayout view, Context context) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public void showDialogError(Context context, String message, CallbackConnection callbackConnection, int code) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alert = builder.create();
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), ((dialogInterface, i) -> {
            alert.cancel();
            callbackConnection.onError(code, message);
        }
        ));
        builder.setPositiveButton(context.getResources().getString(R.string.try_again), (dialog, which) -> {
            alert.cancel();
            callbackConnection.onTryAgain();
        });
        builder.show();
    }

    public void showExpired(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alert = builder.create();
        builder.setMessage(context.getResources().getString(R.string.expired_session));
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getString(R.string.login_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //intentManager.toLogin();
            }
        });
        builder.show();
    }

    public void showUnauthorized(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alert = builder.create();
        builder.setMessage(context.getResources().getString(R.string.expired_session));
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getString(R.string.login_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //intentManager.toLogin();
            }
        });
        builder.show();
    }

    public boolean validateEdittext(EditText editText, int lengthMin, int lengthMax, String error) {
        if (editText.getText().length() < lengthMin || editText.getText().length() > lengthMax) {
            editText.setError(error);
            editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public final static boolean regexOnlyChar(String character) {
        return regex(character, onlyCharUnderScore);
    }

    public final static boolean regexUsername(String character) {
        return regex(character, usernameRegex);
    }


    public final static boolean regex(String input, String regex) {
        final Pattern pattern = Pattern.compile(regex);
        return (pattern.matcher(input).matches());
    }


    public void exitDialog(final Activity context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alert = builder.create();
        builder.setMessage(context.getString(R.string.prompt_exit));
        builder.setCancelable(true);
        builder.setNegativeButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.finish();
            }
        });
        builder.setPositiveButton(context.getString(R.string.no_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.cancel();
            }
        });
        builder.show();
    }


    public void requestFullscreen() {
        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        ((Activity) context).requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void visiblePassword(EditText password, ImageButton showPassword) {
        if (!showPassword.isActivated()) {
            showPassword.setActivated(true);
            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            password.setSelection(password.getText().length());
        } else {
            showPassword.setActivated(false);
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            password.setSelection(password.getText().length());
        }
    }

    public static void copyTextToClipboard(Context context, String message) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Tracking Number", message);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(context, "Nomor Tracking berhasil di salin", Toast.LENGTH_SHORT).show();
    }
}
