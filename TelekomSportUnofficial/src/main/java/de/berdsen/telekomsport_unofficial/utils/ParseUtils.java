package de.berdsen.telekomsport_unofficial.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.berdsen.telekomsport_unofficial.model.EventDetails;
import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.ui.presenter.DefaultCardItem;
import de.berdsen.telekomsport_unofficial.ui.presenter.EventCardItem;

/**
 * Created by berthm on 26.09.2017.
 */

public class ParseUtils {

    public static String loadJsonFromResource(Context context, int resource) {
        if( resource <= 0 || context == null )
            return null;
        String json = null;
        InputStream is = context.getResources().openRawResource( resource );
        try {
            if( is != null ) {
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                json = new String(buffer, "UTF-8");
            }
        } catch( IOException e ) {
            return null;
        } finally {
            try {
                if( is != null )
                    is.close();
            } catch( IOException e ) {}
        }
        return json;
    }

    public static String readHtmlFromInputStream(InputStream in, String encoding) throws IOException {
        // TODO: use encoding from html header
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding), 8);

        // TODO: extract this to utils class
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        in.close();
        return sb.toString();
    }

    public static DefaultCardItem createCardItem(Sport sport) {
        DefaultCardItem cardItem = new DefaultCardItem(sport);
        cardItem.setTitle(sport.getTitle());
        cardItem.setDescription(sport.getTitle());
        cardItem.setImageUrl(sport.getImageUrl());

        return cardItem;
    }

    public static DefaultCardItem createCardItem(String title, String description, int resourceId) {
        DefaultCardItem cardItem = new DefaultCardItem(null);
        cardItem.setTitle(title);
        cardItem.setDescription(description);
        cardItem.setImageResourceId(resourceId);

        return cardItem;
    }

    public static EventCardItem createCardItem(GameEvent event, String baseUrl) {
        EventCardItem cardItem = new EventCardItem(event);

        EventDetails details = event.getMetadata().getDetails();
        String title = details.getHome().getNameShort() + " : " + details.getAway().getNameShort();
        String description = event.getMetadata().getScheduleStart().getOriginal();

        cardItem.setTitle(title);
        cardItem.setDescription(description);
        cardItem.setMainImageUrl(baseUrl + event.getMetadata().getImages().getFallback());
        cardItem.setHomeTeamImageUrl(baseUrl + event.getMetadata().getDetails().getHome().getLogo());
        cardItem.setAwayTeamImageUrl(baseUrl + event.getMetadata().getDetails().getAway().getLogo());

        return cardItem;
    }

}

