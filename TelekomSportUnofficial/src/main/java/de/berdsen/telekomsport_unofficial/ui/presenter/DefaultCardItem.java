package de.berdsen.telekomsport_unofficial.ui.presenter;

import lombok.Data;

/**
 * Created by berthm on 09.10.2017.
 */

@Data
public class DefaultCardItem extends AbstractBaseCardItem {
    private String imageUrl;
    private int imageResourceId;

    public DefaultCardItem(Object item) {
        super(item);
    }
}

