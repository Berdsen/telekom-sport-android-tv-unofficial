package de.berdsen.telekomsport_unofficial.model;

import lombok.Data;

/**
 * Created by berthm on 27.09.2017.
 */

@Data
public final class Sport {
    private final String title;
    private final String imageUrl;
    private final String pageExtension;
    private final String epgExtension;

    Sport(String title, String imageUrl, String pageExtension, String epgExtension) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.pageExtension = pageExtension;
        this.epgExtension = epgExtension;
    }
}
