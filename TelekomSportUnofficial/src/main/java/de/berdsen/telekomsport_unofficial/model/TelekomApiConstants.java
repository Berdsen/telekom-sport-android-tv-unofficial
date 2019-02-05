package de.berdsen.telekomsport_unofficial.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * Created by Berdsen on 27.09.2017.
 */

@Data
public final class TelekomApiConstants implements Serializable {
    private final Date versionDate;
    private final String baseUrl;
    private final String apiUrlExtension;
    private final String loginUrlExtension;
    private final String loginEndpoint;
    private final String videoUrlExtension;
    private final String videoUrlParams;
    private final String bannerImageUrl;
    private final List<Sport> sports;
    private final List<Sport> competitions;

    public TelekomApiConstants(Date versionDate, String baseUrl, String apiUrlExtension, String loginUrlExtension, String loginEndpoint, String videoUrlExtension, String videoUrlParams, String bannerImageUrl, List<Sport> sports, List<Sport> competitions) {
        this.versionDate = versionDate;
        this.baseUrl = baseUrl;
        this.apiUrlExtension = apiUrlExtension;
        this.loginUrlExtension = loginUrlExtension;
        this.loginEndpoint = loginEndpoint;
        this.videoUrlExtension = videoUrlExtension;
        this.videoUrlParams = videoUrlParams;
        this.bannerImageUrl= bannerImageUrl;
        this.sports = sports;
        this.competitions= competitions;
    }

    public void initBaseUrl() {
        for(Sport s : this.sports) {
            s.setBaseUrl(baseUrl);
        }
        for(Sport s : this.competitions) {
            s.setBaseUrl(baseUrl);
        }
    }

    public String getLoginUrl() {
        return String.format(baseUrl + loginUrlExtension, baseUrl);
    }

    public String getVideoUrl(String videoId) {
        return String.format(baseUrl + videoUrlExtension + videoUrlParams, videoId);
    }
}
