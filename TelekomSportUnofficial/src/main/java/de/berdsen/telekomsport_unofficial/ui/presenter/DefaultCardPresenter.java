package de.berdsen.telekomsport_unofficial.ui.presenter;

import android.content.Context;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

/**
 * Created by berthm on 09.10.2017.
 */

public class DefaultCardPresenter extends Presenter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ImageCardView cardView = new ImageCardView( parent.getContext() );
        cardView.setFocusable( true );
        return new DefaultCardPresenterViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        DefaultCardItem cardItem = (DefaultCardItem) item;
        DefaultCardPresenterViewHolder holder = (DefaultCardPresenterViewHolder) viewHolder;
        ImageCardView cardView = holder.getCardView();

        cardView.setTitleText(cardItem.getTitle());
        cardView.setContentDescription(cardItem.getDescription());
        cardView.setMainImageDimensions( 320, 240 );

        if (cardItem.getImageResourceId() != 0) {
            holder.updateCardViewImage( cardView.getContext(), cardItem.getImageResourceId() );
        } else if (!TextUtils.isEmpty(cardItem.getImageUrl())){
            holder.updateCardViewImage( cardView.getContext(), cardItem.getImageUrl() );
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        DefaultCardPresenterViewHolder holder = (DefaultCardPresenterViewHolder) viewHolder;
        ImageCardView cardView = holder.getCardView();

        // TODO: release resources
    }

    private class DefaultCardPresenterViewHolder extends AbstractBaseCardViewHolder {

        public DefaultCardPresenterViewHolder(ImageCardView cardView) {
            super(cardView);
        }

        public void updateCardViewImage(Context context, String link ) {
            Picasso.with(context).load(link)
                    .fit()/*.centerCrop()*/
                    .into(mCardView.getMainImageView());
        }

        public void updateCardViewImage(Context context, int imageId ) {
            Picasso.with(context).load(imageId)
                    .fit()
                    .into(mCardView.getMainImageView());
        }
    }
}
