package com.base.app.domain.utils.dialog;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cahaya on 3/15/17 for MrMontirPOS.
 */

public class ListDialogAdapter extends RecyclerView.Adapter<ListDialogAdapter.DataViewHolder> {

    private ListClickListener mListener;
    private String[] mData;

    ListDialogAdapter(String[] data,
                      ListClickListener listener) {

        mListener = listener;
        mData = data;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_list, parent, false);

        return new DataViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.onBind(mData[position]);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public interface ListClickListener {
        void onListClick(String result);
    }

    class DataViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txvData) TextView txvData;
        private View mItemView;
        private ListClickListener mListener;

        DataViewHolder(View itemView, ListClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mItemView = itemView;
            mListener = listener;
        }

        void onBind(final String data) {
            txvData.setText(data);
            mItemView.setOnClickListener(v -> mListener.onListClick(data));
        }
    }
}
