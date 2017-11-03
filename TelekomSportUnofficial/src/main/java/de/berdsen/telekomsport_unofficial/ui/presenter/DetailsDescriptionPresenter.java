package de.berdsen.telekomsport_unofficial.ui.presenter;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;
import android.text.Html;
import android.util.Log;

import de.berdsen.telekomsport_unofficial.model.BaseContent;
import de.berdsen.telekomsport_unofficial.model.ContentGroup;
import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.model.GameEventDetails;
import de.berdsen.telekomsport_unofficial.model.TextContent;

/**
 * Created by berthm on 01.11.2017.
 */

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    private static final String TAG = "DetailsDescriptionPres";
    private final GameEvent event;

    public DetailsDescriptionPresenter(GameEvent event) {
        this.event = event;
    }

    @Override
    protected void onBindDescription(ViewHolder vh, Object item) {
        GameEventDetails details = (GameEventDetails) item;

        if (details != null) {
            String title = details.getMetadata().getWeb().getTitle();
            if (title.contains("|")) {
                vh.getTitle().setText(Html.fromHtml(title.substring(0, title.indexOf("|"))));
            } else {
                vh.getTitle().setText(title);
            }
            if (event != null) {
                vh.getSubtitle().setText(event.getMetadata().getDescriptionRegular());
            } else {
                vh.getSubtitle().setText("");
            }

            TextContent tc = null;

            for (ContentGroup cg : details.getContentGroups()) {
                for (BaseContent bc : cg.getContentEntries()) {
                    if (!(bc instanceof TextContent)) continue;

                    tc = (TextContent)bc;
                    break;
                }
            }

            if (tc != null) {
                vh.getBody().setText(Html.fromHtml(tc.getText().getText()));
            }

        } else {
            Log.e(TAG, "onBindDescription: Item can not be casted to GameEventDetails");
        }
    }
}
