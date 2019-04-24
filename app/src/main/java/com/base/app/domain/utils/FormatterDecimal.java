package com.base.app.domain.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by faizfiaz on 6/15/2016.
 */
public class FormatterDecimal {
    private static java.text.DecimalFormat decimalFormat;

/*
    public static String decimalFormat(Number textBadge) {
        if (textBadge != null) {
            return new String().format("%,.2f", textBadge);
        }
        return null;
    }
*/

    private static void makeFormatter() {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat = new java.text.DecimalFormat("#,##0", decimalFormatSymbols);
    }

    public static String decimalFormat(Number data) {
        makeFormatter();
        return decimalFormat.format(data);
    }


    public static void wordsCapitalize(final EditText targetET) {
        targetET.addTextChangedListener(new TextWatcher() {
            int mStart = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStart = start + count;
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String capitalizedText;

                if (input.length() < 1)
                    capitalizedText = input;
                else {
                    String[] words = input.split(" ");
                    boolean containSpace = input.charAt(input.length() - 1) == ' ';
                    for (int i = 0; i < words.length; i++) {
                        String capitalizedWord = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
                        words[i] = capitalizedWord;
                    }
                    capitalizedText = TextUtils.join(" ", words);
                    if (containSpace)
                        capitalizedText += " ";
                }
                if (!capitalizedText.equals(targetET.getText().toString())) {
                    targetET.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            targetET.setSelection(mStart);
                            targetET.removeTextChangedListener(this);
                        }
                    });
                    targetET.setText(capitalizedText);
                }
            }
        });
    }

    public static void decimalFormatString(String bodyText, EditText editText) {
        makeFormatter();
        String number = null;
        try {
            number = decimalFormat.format(NumberFormat.getInstance().parse(bodyText));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        editText.setText(number);
    }
}
