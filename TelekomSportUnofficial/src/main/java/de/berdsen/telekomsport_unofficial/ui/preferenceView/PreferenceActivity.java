package de.berdsen.telekomsport_unofficial.ui.preferenceView;

import android.os.Bundle;
import android.support.annotation.Nullable;

import de.berdsen.telekomsport_unofficial.ui.base.AbstractBasePreferenceActivity;

/**
 * Created by berthm on 09.10.2017.
 */

public class PreferenceActivity extends AbstractBasePreferenceActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenceFragment()).commit();
    }

}
