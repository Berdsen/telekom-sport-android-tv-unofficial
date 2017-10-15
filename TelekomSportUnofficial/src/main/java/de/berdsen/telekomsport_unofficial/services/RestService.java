package de.berdsen.telekomsport_unofficial.services;

import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.services.AsyncTasks.LoadEpgTask;
import de.berdsen.telekomsport_unofficial.services.interfaces.EpgResolvedHandler;
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

    public void retrieveSportVideos(Sport lookupSport, EpgResolvedHandler handler) {
        if (handler == null) return;

        LoadEpgTask epgTask = new LoadEpgTask(constants);
        epgTask.handler = handler;
        epgTask.execute(lookupSport);
    }

}
