package de.berdsen.telekomsport_unofficial.ui.sportsVideoView;

import javax.inject.Inject;

import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseBrowseFragment;

/**
 * Created by Berdsen on 14.10.2017.
 */

public class SportsVideoViewFragment extends AbstractBaseBrowseFragment {
    @Inject
    RestService restService;

    @Inject
    SessionService sessionService;

    public static final String SELECTED_SPORTS_URL = "SELECTED_SPORTS_URL_PARAMETER";
}
