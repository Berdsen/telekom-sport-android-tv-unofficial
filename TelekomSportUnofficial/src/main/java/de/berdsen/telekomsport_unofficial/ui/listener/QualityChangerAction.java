package de.berdsen.telekomsport_unofficial.ui.listener;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.util.TypedValue;

/**
 * Created by berthm on 20.11.2017.
 */

public class QualityChangerAction extends PlaybackControlsRow.MultiAction {

    public static final int INDEX_AUTO = 0;
    //public static final int INDEX_LOW = 1;
    //public static final int INDEX_MEDIUM = 2;
    public static final int INDEX_HIGH = 3;

    /**
     * Constructor
     *
     * @param context Context used for loading resources.
     */
    public QualityChangerAction(Context context) {
        super(android.support.v17.leanback.R.id.lb_control_high_quality);
        BitmapDrawable drawable = (BitmapDrawable) getStyledDrawable(context, android.support.v17.leanback.R.styleable.lbPlaybackControlsActionIcons_high_quality);


        Drawable[] drawables = new Drawable[4];
        drawables[INDEX_AUTO] = drawable;
        //drawables[INDEX_LOW] = new BitmapDrawable(context.getResources(), createBitmap(drawable.getBitmap(), Color.BLUE));
        //drawables[INDEX_MEDIUM] = new BitmapDrawable(context.getResources(), createBitmap(drawable.getBitmap(), Color.GREEN));
        drawables[INDEX_HIGH] = new BitmapDrawable(context.getResources(), createBitmap(drawable.getBitmap(), Color.RED));
        setDrawables(drawables);

        String[] labels = new String[4];
        labels[INDEX_AUTO] = "Auto";
        //labels[INDEX_LOW] = "Low";
        //labels[INDEX_MEDIUM] = "Medium";
        labels[INDEX_HIGH] = "High";
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

    static Bitmap createBitmap(Bitmap bitmap, int color) {
        Bitmap dst = bitmap.copy(bitmap.getConfig(), true);
        Canvas canvas = new Canvas(dst);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return dst;
    }
}

