package de.berdsen.telekomsport_unofficial.ui.presenter;

import android.content.Context;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.services.RestService;

/**
 * Created by Berdsen on 08.10.2017.
 */

public class SportsCardPresenter extends Presenter {

    //TODO: make an uri resolver service
    private RestService restService;

    public SportsCardPresenter(RestService restService) {
        this.restService = restService;
    }

    private class InternalViewHolder extends Presenter.ViewHolder {
        private ImageCardView mCardView;

        public InternalViewHolder(View view) {
            super(view);
            mCardView = (ImageCardView) view;
        }

        public ImageCardView getCardView() {
            return mCardView;
        }

        public void updateCardViewImage(Context context, String link ) {
            Picasso.with(context).load(link)
                    .resize(320, 240)/*.centerCrop()*/
                    .into(mCardView.getMainImageView());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ImageCardView cardView = new ImageCardView( parent.getContext() );
        cardView.setFocusable( true );
        return new InternalViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        Sport sport = (Sport) item;
        String imageUrl = restService.getCompleteUrlForExtension(sport.getImageUrlExtension());
        InternalViewHolder holder = (InternalViewHolder) viewHolder;
        ImageCardView cardView = holder.getCardView();

        if ( !TextUtils.isEmpty(imageUrl) ) {
            cardView.setTitleText(sport.getTitle());
            cardView.setMainImageDimensions( 320, 240 );
            holder.updateCardViewImage( holder.getCardView().getContext(), imageUrl );
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }}
