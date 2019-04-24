package com.base.app.domain.utils.dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.app.R;
import com.base.app.domain.utils.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by faizf on 23/04/2018.
 */

public class DialogList extends DialogFragment implements ListDialogAdapter.ListClickListener {

    public static final String PARAM_TITLE = "title";
    public static final String PARAM_DATA = "data";
    @BindView(R.id.rv) RecyclerView rv;
    @BindView(R.id.txvTitle) TextView txvTitle;
    private ListClickListener mListener;

    public static DialogList newInstance(String title, String[] data) {
        DialogList frag = new DialogList();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        args.putStringArray(PARAM_DATA, data);
        frag.setArguments(args);
        return frag;
    }

    public void setListener(ListClickListener listener) {
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_list, container, false);

        // Bind ButterKnife
        ButterKnife.bind(this, rootView);

        assert getArguments() != null;
        txvTitle.setText(getArguments().getString(PARAM_TITLE));

        ListDialogAdapter mAdapter = new ListDialogAdapter(getArguments().getStringArray(PARAM_DATA), this);

        rv.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onListClick(String result) {
        Logger.d("Pongodev-LOG", "DialogList:onListClick -> " + result);

        assert getArguments() != null;
        mListener.onListClicks(result, getArguments().getString(PARAM_TITLE));
        dismiss();
    }

    public interface ListClickListener {
        void onListClicks(String result, String title);
    }
}