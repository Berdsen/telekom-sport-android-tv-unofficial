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

        Runnable r = new Runnable() {
            @Override
            public void run() {
                holder.updateImage(cardView.getContext(), cardItem);
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    private class EventCardPresenterViewHolder extends AbstractBaseCardViewHolder {

        public EventCardPresenterViewHolder(ImageCardView cardView) {
            super(cardView);
        }

        public void updateImage(Context context, EventCardItem cardItem) {
            try {
                Bitmap image = Picasso.with(context).load(cardItem.getMainImageUrl()).resize(320, 240).get();
                Bitmap homeTeam = Picasso.with(context).load(cardItem.getHomeTeamImageUrl()).resize(100, 100).get();
                Bitmap awayTeam = Picasso.with(context).load(cardItem.getAwayTeamImageUrl()).resize(100, 100).get();

                Bitmap result = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
                Canvas canvas = new Canvas(result);

                canvas.drawBitmap(image, 0f, 0f, null);
                canvas.drawBitmap(homeTeam, 20f, 20f, null);
                canvas.drawBitmap(awayTeam, 200f, 20f, null);

                final Drawable drawable = new BitmapDrawable(mCardView.getResources(), result);

                mCardView.post(new Runnable() {
                    @Override
                    public void run() {
                        mCardView.setMainImage(drawable);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
