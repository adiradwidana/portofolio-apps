package com.base.app.domain.utils;

import android.content.Context;
import android.os.Build;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static final double SLICE_ORIGINAL_WIDTH = 700.0;
    public static final double SLICE_ORIGINAL_HEIGHT = 1000.0;
    private static final String PATTERN_ALPHANUMERIC = "^[a-zA-Z0-9]*$";

    public static String listStringToPlainString(List<String> listString) {
        String result = "";
        if (listString != null) {
            for (int i = 0; i < listString.size(); i++) {
                String genre = listString.get(i);
                if (i == listString.size() - 1) {
                    result = result.concat(genre);
                } else {
                    result = result.concat(genre + ", ");
                }
            }
        }
        return result;
    }

    public static boolean checkField(Context context, String value, String message) {
        if ("".equals(value)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean checkFieldEmail(Context context, String value, String message) {
        if ("".equals(value)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            Toast.makeText(context, "Mohon masukan email yang benar", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /*public static boolean checkIsNotDefault(String id) {
        if (!PongodevSession.DEFAULT_ID.equals(id)) {
            return true;
        }
        return false;
    }*/

    /*public static boolean checkPassword(CiayoTextInputLayout tilPassword, String password, Context context) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            tilPassword.setError(context.getString(R.string.error_empty));
            return false;
        } else if (password.length() < 8) {
            tilPassword.setError(context.getString(R.string.error_max_password));
            return false;
        } else {
            Pattern p = Pattern.compile(PATTERN_ALPHANUMERIC);
            if (p.matcher(password).matches()) {
                tilPassword.setValid(context.getString(R.string.input_success));
                return true;
            } else {
                tilPassword.setError(context.getString(R.string.error_password_invalid));
                return false;
            }
        }
    }*/

    @SuppressWarnings("deprecation")
    public static void setTextAppearance(Context context, TextView tv, int resId) {
        if (Build.VERSION.SDK_INT < 23) {
            tv.setTextAppearance(context, resId);
        } else {
            tv.setTextAppearance(resId);
        }
    }


    private static final String EMAIL_ERROR = "Insert Valid Email";
    private static final String INPUT_ERROR = "This Field Must Be Filled";
    private static final String PASSWORD_ERROR = "This Field Must Be Filled minimum 6 character";
    private static final String MIN_ERROR = "This Field Must Be Filled minimum ";
    private static final String CHAR = " character";
    private static final String PASSWORD_NOT_SAME = "Your password doesn't match";

    public static boolean checkEmail(EditText editText) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = editText.getText().toString();
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (editText.getText().toString().equals("") && editText.getText().length() == 0) {
            editText.setError(INPUT_ERROR);
            return false;
        } else {
            if (matcher.matches()) {
                return true;
            } else {
                editText.setError(EMAIL_ERROR);
                editText.requestFocus();
                return false;
            }

        }


    }

    public static boolean checkPassword(EditText editText) {
        if (editText.getText().toString().equals("") || editText.getText().length() < 6) {
            editText.setError(PASSWORD_ERROR);
            editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkEdittext(EditText editText) {
        if (editText.getText().toString().equals("") && editText.getText().length() == 0) {
            editText.setError(INPUT_ERROR);
            editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkEdittextMin(EditText editText, int min) {
        if (editText.getText().toString().equals("") || editText.getText().length() < min) {
            editText.setError(MIN_ERROR + String.valueOf(min) + CHAR);
            editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkPasswordSame(EditText password, EditText passwordConfirm) {
        if (!password.getText().toString().equals(passwordConfirm.getText().toString())) {
            passwordConfirm.setError(PASSWORD_NOT_SAME);
            passwordConfirm.requestFocus();
            return false;
        } else {
            return true;
        }
    }

}
