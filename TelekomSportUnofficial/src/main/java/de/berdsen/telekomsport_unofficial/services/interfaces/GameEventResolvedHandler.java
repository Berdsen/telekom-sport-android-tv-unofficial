package de.berdsen.telekomsport_unofficial.services.interfaces;

import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.model.GameEventDetails;

/**
 * Created by berthm on 20.10.2017.
 */

public interface GameEventResolvedHandler {
    void onGameEventResolved(GameEvent event, GameEventDetails eventDetails);
}
