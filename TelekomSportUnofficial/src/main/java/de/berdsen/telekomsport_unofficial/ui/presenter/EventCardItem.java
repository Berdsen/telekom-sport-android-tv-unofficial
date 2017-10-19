package de.berdsen.telekomsport_unofficial.ui.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.services.ImageCacheService;
import lombok.Data;
import lombok.Getter;

/**
 * Created by berthm on 16.10.2017.
 */

@Data
public class EventCardItem extends AbstractBaseCardItem {
    private String mainImageUrl;
    private String homeTeamImageUrl;
    private String awayTeamImageUrl;

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
