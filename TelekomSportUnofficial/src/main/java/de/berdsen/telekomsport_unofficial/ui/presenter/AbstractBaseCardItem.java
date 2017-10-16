package de.berdsen.telekomsport_unofficial.ui.presenter;

import lombok.Data;

/**
 * Created by berthm on 16.10.2017.
 */

@Data
public class AbstractBaseCardItem {
    private String title;
    private String description;
    private final Object item;

    public AbstractBaseCardItem(Object item) {
        this.item = item;
    }
}
