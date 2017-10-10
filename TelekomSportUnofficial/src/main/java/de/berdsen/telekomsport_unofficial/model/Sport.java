package de.berdsen.telekomsport_unofficial.model;

import lombok.Data;

/**
 * Created by berthm on 27.09.2017.
 */

@Data
public final class Sport {
    private final String title;
    private final String imageUrlExtension;
    private final String pageExtension;
    private final String epgExtension;

    private String baseUrl;

    Sport(String title, String imageUrlExtension, String pageExtension, String epgExtension) {
        this.title = title;
        this.imageUrlExtension = imageUrlExtension;
        this.pageExtension = pageExtension;
        this.epgExtension = epgExtension;
    }

    public String getImageUrl() {
        if (this.imageUrlExtension.toLowerCase().startsWith("http")) {
            // url seems to be rooted
            return this.imageUrlExtension;
        }

        return baseUrl + imageUrlExtension;
    }
}
