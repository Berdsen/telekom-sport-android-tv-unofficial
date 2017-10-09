package de.berdsen.telekomsport_unofficial.ui.preferenceView;

import android.os.Bundle;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBasePreferenceFragment;

/**
 * Created by berthm on 09.10.2017.
 */

public class PreferenceFragment extends AbstractBasePreferenceFragment
{
    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}