package de.berdsen.telekomsport_unofficial.ui.selectedVideoDetailsView;

import android.os.Bundle;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SportsService;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseDetailsFragment;

/**
 * Created by berthm on 19.10.2017.
 */

public class SelectedVideoDetailsFragment extends AbstractBaseDetailsFragment {

    @Inject
    RestService restService;

    @Inject
    SportsService sportsService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (sportsService.getSelectedGameEvent() == null) {
            return;
        }
    }

}
