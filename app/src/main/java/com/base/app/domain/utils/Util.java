package com.base.app.domain.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by faizf on 2/25/2017.
 */
public class Util {

    public Calendar myCalendar;
    public DatePickerDialog.OnDateSetListener dates;

    public void showCalendar(final EditText editText, Activity activity) {
        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dates = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText);
            }

        };

        new DatePickerDialog(activity, dates, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void updateLabel(EditText date) {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(sdf.format(myCalendar.getTime()));
    }

    public void showToastConstruction(Activity activity){
        Toast.makeText(activity, "This feature is under construction", Toast.LENGTH_SHORT).show();
    }

    public void showCustomToast(Activity activity, String message){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public boolean checkListContent(int size, RecyclerView list, LinearLayout empty) {
        if (size == 0){
            list.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            return false;
        }else {
            list.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            return true;
        }
    }

    public void changeNumberFormat(Editable s, EditText harvestTotal, TextWatcher textWatcher) {
        harvestTotal.removeTextChangedListener(textWatcher);
        try {
            String givenstring = s.toString();
            Long longval;
            if (givenstring.contains(",")) {
                givenstring = givenstring.replaceAll(",", "");
            }
            longval = Long.parseLong(givenstring);
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
            DecimalFormat formatter = (DecimalFormat) nf;
            formatter.applyPattern("#,###,###");
            String formattedString = formatter.format(longval);
            harvestTotal.setText(formattedString);
            harvestTotal.setSelection(harvestTotal.getText().length());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        harvestTotal.addTextChangedListener(textWatcher);
    }

}
