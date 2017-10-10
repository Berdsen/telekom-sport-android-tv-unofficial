package de.berdsen.telekomsport_unofficial.ui.presenter;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by berthm on 09.10.2017.
 */

@Data
public class DefaultCardItem {
    private String title;
    private String description;
    private String imageUrl;
    private int imageResourceId;
    private final Object item;

    public DefaultCardItem(Object item) {
        this.item = item;
    }
}

