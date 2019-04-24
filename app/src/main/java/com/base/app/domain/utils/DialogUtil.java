package com.base.app.domain.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.app.R;
import com.base.app.domain.utils.graphic.DimensionUtil;
import static android.R.id.message;
/**
 * Created by cahaya on 1/20/17.
 */

public class DialogUtil {
    public static AlertDialog styleAndShowAlertDialog(AlertDialog alertDialog, String title) {

        // style title
        /*int titleId = alertDialog.getContext().getResources().getIdentifier( "alertTitle", "id", "android" );
        if (titleId > 0) {
            DialogTitle dialogTitle = (DialogTitle) alertDialog.findViewById(titleId);
            if (dialogTitle != null) {
                dialogTitle.setTypeface(face);
            }
        }*/

        if (title != null) {
            int padding = (int) DimensionUtil.dpToPx(alertDialog.getContext().getResources(), 24);
            TextView textView = new TextView(alertDialog.getContext());
            textView.setText(title);
            //setTextAppearance(alertDialog.getContext(), textView, R.style.TextAppearance_NexaHeavy18);
            textView.setPadding(padding, padding, padding, 0);
            textView.setTextColor(Color.BLACK);
            alertDialog.setCustomTitle(textView);
            alertDialog.dismiss();
        }
        alertDialog.show();

        // style message
        TextView tvMessage = (TextView) alertDialog.findViewById(message);
        if (tvMessage != null) {
            //setTextAppearance(alertDialog.getContext(), tvMessage, R.style.TextAppearance_DialogMessage);
            tvMessage.setTextColor(ContextCompat.getColor(alertDialog.getContext(), R.color.black));
            tvMessage.setLineSpacing(DimensionUtil.spToPx(alertDialog.getContext().getResources(), 6), 1);
        }

        return alertDialog;

    }

    @SuppressLint("InflateParams")
    public static void showLoginDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialog);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.state_layout_dialog_invalid_token, null);
        final AlertDialog dialog = builder.create();
        dialog.setView(inflate, 16, 32, 16, 16);
        dialog.setCancelable(false);
        Button mBtnLogin = (Button) inflate.findViewById(R.id.state_layout_btn_reload);
        mBtnLogin.setOnClickListener(view -> {
            dialog.dismiss();
            //BaseApp.logout(activity);
//            PongodevSession.getInstance().logout(activity);

        });

        DialogUtil.styleAndShowAlertDialog(dialog, null);
    }


}
