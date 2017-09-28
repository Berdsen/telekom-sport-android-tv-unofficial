package de.berdsen.telekomsport_unofficial.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseActivity;

public class OverviewActivity extends AbstractBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
    }

}
