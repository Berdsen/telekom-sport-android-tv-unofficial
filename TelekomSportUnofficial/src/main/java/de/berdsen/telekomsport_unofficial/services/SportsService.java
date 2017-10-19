package de.berdsen.telekomsport_unofficial.services;

import de.berdsen.telekomsport_unofficial.model.GameEvent;

/**
 * Created by berthm on 19.10.2017.
 */

public class SportsService {

    private GameEvent selectedGameEvent;

    public void setSelectedGameEvent(GameEvent event) {
        this.selectedGameEvent = event;
    }

    public GameEvent getSelectedGameEvent() {
        return this.selectedGameEvent;
    }

}
