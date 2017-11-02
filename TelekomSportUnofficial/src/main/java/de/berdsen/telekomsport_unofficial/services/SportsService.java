package de.berdsen.telekomsport_unofficial.services;

import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.model.GameEventDetails;
import de.berdsen.telekomsport_unofficial.model.VideoDetails;

/**
 * Created by berthm on 19.10.2017.
 */

public class SportsService {

    private GameEvent selectedGameEvent;
    private GameEventDetails selectedGameEventDetails;
    private VideoDetails selectedVideo;

    public void setGameEvent(GameEvent event, GameEventDetails gameEventDetails) {
        this.selectedGameEvent = event;
        this.selectedGameEventDetails = gameEventDetails;
    }

    public GameEvent getGameEvent() {
        return this.selectedGameEvent;
    }

    public GameEventDetails getGameEventDetails() {
        return this.selectedGameEventDetails;
    }

    public void setSelectedVideo(VideoDetails selectedVideo) {
        this.selectedVideo = selectedVideo;
    }

    public VideoDetails getSelectedVideo() {
        return this.selectedVideo;
    }
}
