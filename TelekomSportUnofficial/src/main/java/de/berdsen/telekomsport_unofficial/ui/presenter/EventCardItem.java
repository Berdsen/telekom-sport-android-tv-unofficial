package de.berdsen.telekomsport_unofficial.ui.presenter;

import android.graphics.drawable.Drawable;

import de.berdsen.telekomsport_unofficial.model.GameEvent;
import lombok.Data;
import lombok.Getter;

/**
 * Created by Berdsen on 16.10.2017.
 */

@Data
public class EventCardItem extends AbstractBaseCardItem {
    private String mainImageUrl;
    private String homeTeamImageUrl;
    private String awayTeamImageUrl;
    private String dateTime;

    @Getter
    private Drawable createdDrawable;

    public EventCardItem(Object item) {
        super(item);
    }

    public void setCreatedDrawable(Drawable drawable) {
        this.createdDrawable = drawable;
        if (((GameEvent)item) != null) {
            ((GameEvent)item).setCreatedDrawable(drawable);
        }
    }
}
