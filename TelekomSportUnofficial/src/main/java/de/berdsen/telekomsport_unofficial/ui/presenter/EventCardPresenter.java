package de.berdsen.telekomsport_unofficial.ui.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import de.berdsen.telekomsport_unofficial.services.PicassoCache;

/**
 * Created by berthm on 16.10.2017.
 */

//TODO: implement this
public class EventCardPresenter extends Presenter {

    private final PicassoCache picassoCache;

    public EventCardPresenter(PicassoCache picassoCache) {
        this.picassoCache = picassoCache;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ImageCardView cardView = new ImageCardView( parent.getContext() );
        cardView.setFocusable( true );
        return new EventCardPresenterViewHolder(cardView, picassoCache);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        final EventCardItem cardItem = (EventCardItem) item;
        final EventCardPresenterViewHolder holder = (EventCardPresenterViewHolder) viewHolder;
        final ImageCardView cardView = holder.getCardView();

        cardView.setTitleText(cardItem.getTitle());
        cardView.setContentText(cardItem.getDescription());
        cardView.setMainImageDimensions( 320, 240 );

        holder.updateImage(cardView.getContext(), cardItem);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    private class EventCardPresenterViewHolder extends AbstractBaseCardViewHolder {

        private Picasso picasso;

        public EventCardPresenterViewHolder(ImageCardView cardView, PicassoCache picassoCache) {
            super(cardView);
            this.picasso = picassoCache.getPicassoCacheInstance();
        }

        public void updateImage(final Context context, final EventCardItem cardItem) {
            final Handler handler = new Handler(context.getMainLooper());

            Thread loadImages = new Thread(new Runnable() {
                @Override
                public void run() {
                    final Drawable drawable;
                    try {
                        Drawable alreadyExistentDrawable = cardItem.getCreatedDrawable();
                        if (alreadyExistentDrawable == null) {
                            drawable = resolveImage(context, cardItem);
                            cardItem.setCreatedDrawable(drawable);
                        } else {
                            drawable = alreadyExistentDrawable;
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mCardView.setMainImage(drawable);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                        //TODO: set default image
                    }
                }
            });

            loadImages.start();
        }

        private Drawable resolveImage(Context context, EventCardItem cardItem) throws IOException {

            Bitmap image = picasso.get().load(cardItem.getMainImageUrl()).resize(320, 240).get();

            if (cardItem.getHomeTeamImageUrl() == null || cardItem.getAwayTeamImageUrl() == null) {
                Bitmap result = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
                Canvas canvas = new Canvas(result);

                canvas.drawBitmap(image, 0f, 0f, null);
                return new BitmapDrawable(context.getResources(), result);
            }

            Bitmap homeTeam = picasso.get().load(cardItem.getHomeTeamImageUrl()).resize(100, 100).get();
            Bitmap awayTeam = picasso.get().load(cardItem.getAwayTeamImageUrl()).resize(100, 100).get();

            Bitmap dateTime = createTextBitmap(320, 100, cardItem.getDateTime());

            Bitmap result = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());

            Canvas canvas = new Canvas(result);

            canvas.drawBitmap(image, 0f, 0f, null);
            canvas.drawBitmap(homeTeam, 20f, 20f, null);
            canvas.drawBitmap(awayTeam, 200f, 20f, null);
            canvas.drawBitmap(dateTime, 0f, 120f, null);

            return new BitmapDrawable(context.getResources(), result);
        }

        /**
         * taken from https://stackoverflow.com/a/16150718
         * @param width
         * @param height
         * @param text
         * @return
         */
        private Bitmap createTextBitmap(int width, int height, String text) {
            Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
            Canvas c = new Canvas(b);
            c.drawBitmap(b, 0, 0, null);
            TextPaint textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(30.0F);
            textPaint.setFakeBoldText(true);
            textPaint.setColor(Color.YELLOW);
            StaticLayout sl= new StaticLayout(text, textPaint, b.getWidth()-8, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            c.translate(6, 40);
            sl.draw(c);
            return b;
        }

    }
}

