package de.berdsen.telekomsport_unofficial.ui.presenter;

import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;

/**
 * Created by berthm on 16.10.2017.
 */

public class AbstractBaseCardViewHolder extends Presenter.ViewHolder {

    protected ImageCardView mCardView;

    public AbstractBaseCardViewHolder(ImageCardView cardView) {
        super(cardView);
        mCardView = cardView;
    }

    public ImageCardView getCardView() {
        return mCardView;
    }

}
