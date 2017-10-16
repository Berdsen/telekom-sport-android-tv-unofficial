package de.berdsen.telekomsport_unofficial.ui.presenter;

import lombok.Data;

/**
 * Created by berthm on 16.10.2017.
 */

@Data
public class EventCardItem extends AbstractBaseCardItem {
    private String mainImageUrl;
    private String homeTeamImageUrl;
    private String awayTeamImageUrl;

    public EventCardItem(Object item) {
        super(item);
    }
}
