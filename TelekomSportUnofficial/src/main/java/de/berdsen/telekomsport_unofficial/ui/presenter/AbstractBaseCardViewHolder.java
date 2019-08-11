package de.berdsen.telekomsport_unofficial.ui.presenter;

import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

/**
 * Created by Berdsen on 16.10.2017.
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
