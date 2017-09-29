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
    private final String loginUrlExtension;
    private final String loginEndpoint;
    private final String videoUrlExtension;
    private final String videoUrlParams;
    private final List<Sport> sports;

    TelekomApiConstants(String baseUrl, String apiUrlExtension, String loginUrlExtension, String loginEndpoint, String videoUrlExtension, String videoUrlParams, List<Sport> sports) {
        this.baseUrl = baseUrl;
        this.apiUrlExtension = apiUrlExtension;
        this.loginUrlExtension = loginUrlExtension;
        this.loginEndpoint = loginEndpoint;
        this.videoUrlExtension = videoUrlExtension;
        this.videoUrlParams = videoUrlParams;
        this.sports = sports;
    }

    public String getLoginUrl() {
        return String.format(baseUrl + loginUrlExtension, baseUrl);
    }

    public String getVideoUrl(String videoId) {
        return String.format(baseUrl + videoUrlExtension + videoUrlParams, videoId);
    }
}
