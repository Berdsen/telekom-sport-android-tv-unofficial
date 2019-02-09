package de.berdsen.telekomsport_unofficial.ui.listener;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.util.TypedValue;

import de.berdsen.telekomsport_unofficial.utils.ApplicationUtils;

/**
 * Created by Berdsen on 20.11.2017.
 */

public class QualityChangerAction extends PlaybackControlsRow.MultiAction {

    public static final int INDEX_AUTO = 0;

    /**
     * Constructor
     *
     * @param context Context used for loading resources.
     */
    public QualityChangerAction(Context context) {
        super(android.support.v17.leanback.R.id.lb_control_high_quality);
        BitmapDrawable drawable = (BitmapDrawable) getStyledDrawable(context, android.support.v17.leanback.R.styleable.lbPlaybackControlsActionIcons_high_quality);

        Drawable[] drawables = new Drawable[1];
        drawables[INDEX_AUTO] = new BitmapDrawable(context.getResources(), ApplicationUtils.colorBitmap(drawable.getBitmap(), Color.RED));
        setDrawables(drawables);

        String[] labels = new String[1];
        labels[INDEX_AUTO] = "QualityChooser";
        setLabels(labels);
    }

    static Drawable getStyledDrawable(Context context, int index) {
        TypedValue outValue = new TypedValue();
        if (!context.getTheme().resolveAttribute(
                android.support.v17.leanback.R.attr.playbackControlsActionIcons, outValue, false)) {
            return null;
        }
        TypedArray array = context.getTheme().obtainStyledAttributes(outValue.data,
                android.support.v17.leanback.R.styleable.lbPlaybackControlsActionIcons);
        Drawable drawable = array.getDrawable(index);
        array.recycle();
        return drawable;
    }
}

