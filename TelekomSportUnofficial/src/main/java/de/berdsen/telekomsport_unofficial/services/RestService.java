package de.berdsen.telekomsport_unofficial.services;

import android.net.Uri;
import androidx.annotation.NonNull;

import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.services.AsyncTasks.GetVideoUrlTask;
import de.berdsen.telekomsport_unofficial.services.AsyncTasks.LoadEpgTask;
import de.berdsen.telekomsport_unofficial.services.AsyncTasks.LoadGameEventTask;
import de.berdsen.telekomsport_unofficial.services.interfaces.EpgResolvedHandler;
import de.berdsen.telekomsport_unofficial.services.interfaces.GameEventResolvedHandler;
import de.berdsen.telekomsport_unofficial.services.interfaces.SportsResolvedHandler;
import de.berdsen.telekomsport_unofficial.services.interfaces.VideoUrlResolvedHandler;

/**
 * Created by Berdsen on 27.09.2017.
 */

public class RestService {

    private final SessionService sessionService;
    private final TelekomApiConstants constants;

    public RestService(TelekomApiConstants constants, SessionService sessionService) {
        this.constants = constants;
        this.sessionService = sessionService;
    }

    public void retrieveSportsList(@NonNull SportsResolvedHandler handler) {
        handler.resolvedSports(constants.getSports(), constants.getCompetitions());
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

    public void retrieveVideoUrl(@NonNull Uri mediaSourceUri, @NonNull VideoUrlResolvedHandler handler ) {
        GetVideoUrlTask videoUrlTask = new GetVideoUrlTask(sessionService, constants);
        videoUrlTask.handler = handler;
        videoUrlTask.execute(mediaSourceUri);
    }
}
