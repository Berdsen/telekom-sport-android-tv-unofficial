package de.berdsen.telekomsport_unofficial.ui.presenter;

import android.content.Context;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import de.berdsen.telekomsport_unofficial.model.Video;

/**
 * Created by berthm on 26.09.2017.
 */

public class CardPresenter extends Presenter {

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
                    .resize(210, 210).centerCrop()
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
        Video video = (Video) item;
        if ( !TextUtils.isEmpty(video.getPoster()) ) {
            ((InternalViewHolder) viewHolder).mCardView
                    .setTitleText(video.getTitle());
            ((InternalViewHolder) viewHolder).mCardView
                    .setMainImageDimensions( 210, 210 );
            ( (InternalViewHolder) viewHolder )
                    .updateCardViewImage( ( (InternalViewHolder) viewHolder )
                            .getCardView().getContext(), video.getPoster() );
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
