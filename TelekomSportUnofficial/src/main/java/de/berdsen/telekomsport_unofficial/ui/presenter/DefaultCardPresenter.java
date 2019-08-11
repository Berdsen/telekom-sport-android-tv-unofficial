package de.berdsen.telekomsport_unofficial.ui.presenter;

 import android.graphics.Color;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.berdsen.telekomsport_unofficial.services.PicassoCache;

/**
 * Created by Berdsen on 09.10.2017.
 */

public class DefaultCardPresenter extends Presenter {

    private PicassoCache picassoCache;
    private final int WIDTH = 320;
    private final int HEIGHT = 240;
    private final int PADDING = 5;

    public DefaultCardPresenter(PicassoCache picassoCache) {
        this.picassoCache = picassoCache;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ImageCardView cardView = new ImageCardView( parent.getContext() );
        cardView.setFocusable( true );
        return new DefaultCardPresenterViewHolder(cardView, picassoCache);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        DefaultCardItem cardItem = (DefaultCardItem) item;
        DefaultCardPresenterViewHolder holder = (DefaultCardPresenterViewHolder) viewHolder;
        ImageCardView cardView = holder.getCardView();

        cardView.getMainImageView().setScaleType(ImageView.ScaleType.CENTER);
        cardView.setBackgroundColor(Color.TRANSPARENT);

        cardView.setTitleText(cardItem.getTitle());
        cardView.setContentDescription(cardItem.getDescription());
        cardView.setMainImageDimensions( WIDTH, HEIGHT);

        if (cardItem.getImageResourceId() != 0) {
            holder.updateCardViewImage( cardItem.getImageResourceId() );
        } else if (!TextUtils.isEmpty(cardItem.getImageUrl())){
            holder.updateCardViewImage( cardItem.getImageUrl() );
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        DefaultCardPresenterViewHolder holder = (DefaultCardPresenterViewHolder) viewHolder;
        ImageCardView cardView = holder.getCardView();

        // TODO: release resources
    }

    private class DefaultCardPresenterViewHolder extends AbstractBaseCardViewHolder {

        private Picasso picasso;

        public DefaultCardPresenterViewHolder(ImageCardView cardView, PicassoCache picassoCache) {
            super(cardView);
            picasso = picassoCache.getPicassoCacheInstance();
        }

        public void updateCardViewImage(String link ) {
            picasso.load(link)
                    .resize(WIDTH - PADDING, HEIGHT - PADDING)
                    .centerInside()
                    .into(mCardView.getMainImageView());
        }

        public void updateCardViewImage(int imageId ) {
            picasso.load(imageId)
                    .resize(WIDTH - PADDING, HEIGHT - PADDING)
                    .centerInside()
                    .into(mCardView.getMainImageView());
        }
    }
}
