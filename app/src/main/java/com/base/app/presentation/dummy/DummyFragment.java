package com.base.app.presentation.dummy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.base.app.R;
import com.base.app.domain.ui.PongodevStateView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by cahayaPangriptaAlam on 10/09/18.
 */
public class DummyFragment extends Fragment {

    @BindView(R.id.root) RelativeLayout root;
    Unbinder unbinder;

    //TODO required empty constructor
    public DummyFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static DummyFragment newInstance() {
        DummyFragment fragment = new DummyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dummy, container, false);
        unbinder = ButterKnife.bind(this, view);

        PongodevStateView stateView = PongodevStateView.inject(root);
        stateView.showError(PongodevStateView.ERROR_UNDER_CONSTRACTIONS);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
