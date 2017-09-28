package de.berdsen.telekomsport_unofficial.ui.presenter;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import de.berdsen.telekomsport_unofficial.model.Video;

/**
 * Created by berthm on 28.09.2017.
 */

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(AbstractDetailsDescriptionPresenter.ViewHolder viewHolder, Object item) {
        Video video = (Video) item;
        if (video != null) {
            viewHolder.getTitle().setText(video.getTitle());
            viewHolder.getSubtitle().setText(video.getCategory());
            viewHolder.getBody().setText(video.getDescription());
        }
    }
}


