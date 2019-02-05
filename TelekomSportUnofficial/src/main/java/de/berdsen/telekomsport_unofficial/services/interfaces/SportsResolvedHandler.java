package de.berdsen.telekomsport_unofficial.services.interfaces;

import java.util.List;

import de.berdsen.telekomsport_unofficial.model.Sport;

/**
 * Created by Berdsen on 06.10.2017.
 */

public interface SportsResolvedHandler {
     void resolvedSports(List<Sport> sports, List<Sport> competitions);
}
