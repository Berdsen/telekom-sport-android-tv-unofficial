package de.berdsen.telekomsport_unofficial.services.interfaces;

import java.util.List;

import de.berdsen.telekomsport_unofficial.model.EpgData;
import de.berdsen.telekomsport_unofficial.model.Sport;

/**
 * Created by Berdsen on 15.10.2017.
 */

public interface EpgResolvedHandler {
    void epgDataResolved(Sport resolvedSport, List<EpgData> epgList);
}
