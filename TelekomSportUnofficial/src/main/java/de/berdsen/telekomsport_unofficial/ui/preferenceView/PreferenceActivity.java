package de.berdsen.telekomsport_unofficial.ui.preferenceView;

import android.os.Bundle;
import android.support.annotation.Nullable;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseActivity;

/**
 * Created by berthm on 09.10.2017.
 */

public class PreferenceActivity extends AbstractBaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
    }

}
