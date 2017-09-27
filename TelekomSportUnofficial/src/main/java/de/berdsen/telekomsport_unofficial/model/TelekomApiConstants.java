package de.berdsen.telekomsport_unofficial.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by berthm on 27.09.2017.
 */

@Data
public final class TelekomApiConstants implements Serializable {
    private final String baseUrl;
    private final String apiUrlExtension;
    private final List<Sport> sports;

    TelekomApiConstants(String baseUrl, String apiUrlExtension, List<Sport> sports) {
        this.baseUrl = baseUrl;
        this.apiUrlExtension = apiUrlExtension;
        this.sports = sports;
    }
}
