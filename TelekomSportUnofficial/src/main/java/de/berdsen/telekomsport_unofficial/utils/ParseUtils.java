package de.berdsen.telekomsport_unofficial.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

    public static boolean isNullOrWhitespace(String input) {
        if (input == null || input.trim().equals("")) {
            return true;
        }
        return false;
    }

    public static EventCardItem createCardItem(GameEvent event, String baseUrl) {
        EventCardItem cardItem = new EventCardItem(event);

        EventDetails details = event.getMetadata().getDetails();

        String title;
        String description;

        String homeTeamName =
                details.getHome() != null && !isNullOrWhitespace(details.getHome().getNameShort())
                        ? details.getHome().getNameShort()
                        : "";

        String awayTeamName =
                details.getAway() != null && !isNullOrWhitespace(details.getAway().getNameShort())
                        ? details.getAway().getNameShort()
                        : "";

        if (isNullOrWhitespace(homeTeamName) || isNullOrWhitespace(awayTeamName)) {
            title = event.getMetadata().getTitle();
            description = event.getMetadata().getDescriptionRegular();
            cardItem.setDateTime(description);
        } else {

            title = parseDateTime(event.getMetadata().getScheduleStart().getOriginal(), "dd.MM.yyyy HH:mm");
            description = homeTeamName + " : " + awayTeamName;
            cardItem.setDateTime(parseDateTime(event.getMetadata().getScheduleStart().getOriginal(), "dd.MM HH:mm"));
        }

        cardItem.setTitle(title);
        cardItem.setDescription(description);

        if (isNullOrWhitespace(event.getMetadata().getImages().getEditorial())) {
            cardItem.setMainImageUrl(baseUrl + event.getMetadata().getImages().getFallback());
            try {
                cardItem.setHomeTeamImageUrl(baseUrl + event.getMetadata().getDetails().getHome().getLogo());
                cardItem.setAwayTeamImageUrl(baseUrl + event.getMetadata().getDetails().getAway().getLogo());
            } catch (NullPointerException npe) {
                // TODO: log this. I was a little bit lazy with checking if something is null
            }
        } else {
            cardItem.setMainImageUrl(baseUrl + event.getMetadata().getImages().getEditorial());
        }

        return cardItem;
    }

    private static String parseDateTime(String possibleDateTimeString) {
        return parseDateTime(possibleDateTimeString, "dd.MM. HH:mm");
    }

    private static String parseDateTime(String possibleDateTimeString, String toPattern) {
        return parseDateTime(possibleDateTimeString, "yyyy-MM-dd HH:mm:ss", toPattern);
    }

    private static String parseDateTime(String possibleDateTimeString, String fromPattern, String toPattern) {
        // retrieval format
        SimpleDateFormat format = new SimpleDateFormat(fromPattern);

        // conversion format
        SimpleDateFormat format2 = new SimpleDateFormat(toPattern);

        try {
            return format2.format(format.parse(possibleDateTimeString));
        } catch (ParseException e) {
            return possibleDateTimeString;
        }
    }
}

