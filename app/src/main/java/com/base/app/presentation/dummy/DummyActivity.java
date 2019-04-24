package com.base.app.presentation.dummy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.base.app.R;
import com.base.app.domain.ui.PongodevStateView;
import com.base.app.domain.utils.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by cahayaPangriptaAlam on 30/09/18.
 */
public class DummyActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "extra_title";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.frmRoot) FrameLayout frmRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Timber.e("keong: %s", getIntent().getBundleExtra(EXTRA_TITLE));
        Logger.d("Pongodev-LOG", "ProfileFragment:setUnderConstrations 6-> "+getIntent().getStringExtra(EXTRA_TITLE));
        getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_TITLE));
        PongodevStateView stateView = PongodevStateView.inject(frmRoot);
        stateView.showError(PongodevStateView.ERROR_UNDER_CONSTRACTIONS);
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
}
