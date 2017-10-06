package de.berdsen.telekomsport_unofficial.services;

import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.services.interfaces.SportsResolvedHandler;

/**
 * Created by berthm on 27.09.2017.
 */

public class RestService {

    private TelekomApiConstants constants;

    public RestService(TelekomApiConstants constants) {
        this.constants = constants;
    }

    public void retrieveSportsList(SportsResolvedHandler handler) {
        if (handler == null) return;

        handler.resolvedSports(constants.getSports());
    }
}
