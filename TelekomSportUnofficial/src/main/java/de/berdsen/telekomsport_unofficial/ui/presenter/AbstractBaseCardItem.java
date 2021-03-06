package de.berdsen.telekomsport_unofficial.ui.presenter;

import lombok.Data;

/**
 * Created by Berdsen on 16.10.2017.
 */

@Data
public class AbstractBaseCardItem {
    private String title;
    private String description;
    protected final Object item;

    public AbstractBaseCardItem(Object item) {
        this.item = item;
    }
}
