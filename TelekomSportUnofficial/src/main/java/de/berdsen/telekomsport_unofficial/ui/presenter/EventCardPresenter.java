package de.berdsen.telekomsport_unofficial.ui.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

/**
 * Created by berthm on 16.10.2017.
 */

//TODO: implement this
public class EventCardPresenter extends Presenter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ImageCardView cardView = new ImageCardView( parent.getContext() );
        cardView.setFocusable( true );
        return new EventCardPresenterViewHolder(cardView);
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

        public EventCardPresenterViewHolder(ImageCardView cardView) {
            super(cardView);
        }

        public void updateImage(final Context context, final EventCardItem cardItem) {
            Thread loadImages = new Thread(new Runnable() {
                @Override
                public void run() {
                    final Drawable drawable;
                    try {
                        drawable = resolveImages(context, cardItem);

                        mCardView.post(new Runnable() {
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

        private Drawable resolveImages(Context context, EventCardItem cardItem) throws IOException {

            File mainImageFile = cardItem.getImageCacheService().retrieveFileFromUrl(cardItem.getMainImageUrl(), context);
            File homeTeamFile = cardItem.getImageCacheService().retrieveFileFromUrl(cardItem.getHomeTeamImageUrl(), context);
            File awayTeamFile = cardItem.getImageCacheService().retrieveFileFromUrl(cardItem.getAwayTeamImageUrl(), context);

            Bitmap image = Picasso.with(context).load(mainImageFile).resize(320, 240).get();
            Bitmap homeTeam = Picasso.with(context).load(homeTeamFile).resize(100, 100).get();
            Bitmap awayTeam = Picasso.with(context).load(awayTeamFile).resize(100, 100).get();

            Bitmap result = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
            Canvas canvas = new Canvas(result);

            canvas.drawBitmap(image, 0f, 0f, null);
            canvas.drawBitmap(homeTeam, 20f, 20f, null);
            canvas.drawBitmap(awayTeam, 200f, 20f, null);

            return new BitmapDrawable(mCardView.getResources(), result);
        }
    }
}
