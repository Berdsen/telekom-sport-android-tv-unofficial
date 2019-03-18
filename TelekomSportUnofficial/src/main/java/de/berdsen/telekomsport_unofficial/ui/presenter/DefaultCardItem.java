package de.berdsen.telekomsport_unofficial.ui.presenter;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Berdsen on 09.10.2017.
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class DefaultCardItem extends AbstractBaseCardItem {
    private String imageUrl;
    private int imageResourceId;

    public DefaultCardItem(Object item) {
        super(item);
    }
}

