package de.berdsen.telekomsport_unofficial.ui.presenter;

import de.berdsen.telekomsport_unofficial.services.ImageCacheService;
import lombok.Data;

/**
 * Created by berthm on 16.10.2017.
 */

@Data
public class EventCardItem extends AbstractBaseCardItem {
    private String mainImageUrl;
    private String homeTeamImageUrl;
    private String awayTeamImageUrl;
    private ImageCacheService imageCacheService;

    public EventCardItem(Object item, ImageCacheService imageCacheService) {
        super(item);
        this.imageCacheService = imageCacheService;
    }
}
