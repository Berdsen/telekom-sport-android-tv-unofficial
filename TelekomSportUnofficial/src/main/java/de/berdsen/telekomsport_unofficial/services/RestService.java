package de.berdsen.telekomsport_unofficial.services;

import android.support.annotation.NonNull;

import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.services.AsyncTasks.LoadEpgTask;
import de.berdsen.telekomsport_unofficial.services.AsyncTasks.LoadGameEventTask;
import de.berdsen.telekomsport_unofficial.services.interfaces.EpgResolvedHandler;
import de.berdsen.telekomsport_unofficial.services.interfaces.GameEventResolvedHandler;
import de.berdsen.telekomsport_unofficial.services.interfaces.SportsResolvedHandler;

/**
 * Created by berthm on 27.09.2017.
 */

public class RestService {

    private TelekomApiConstants constants;

    public RestService(TelekomApiConstants constants) {
        this.constants = constants;
    }

    public void retrieveSportsList(@NonNull SportsResolvedHandler handler) {
        handler.resolvedSports(constants.getSports());
    }

    public void retrieveSportVideos(@NonNull Sport lookupSport, @NonNull EpgResolvedHandler handler) {
        LoadEpgTask epgTask = new LoadEpgTask(constants);
        epgTask.handler = handler;
        epgTask.execute(lookupSport);
    }

    public void retrieveEventDetails(@NonNull GameEvent gameEvent, @NonNull GameEventResolvedHandler handler) {
        LoadGameEventTask gameEventTask = new LoadGameEventTask(constants);
        gameEventTask.handler = handler;
        gameEventTask.execute(gameEvent);
    }
}
