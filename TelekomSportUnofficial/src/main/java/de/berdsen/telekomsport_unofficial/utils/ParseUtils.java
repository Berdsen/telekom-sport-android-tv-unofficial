package de.berdsen.telekomsport_unofficial.utils;

import android.content.Context;
import android.preference.Preference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.ui.presenter.DefaultCardItem;

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

    public static DefaultCardItem createDefaultCardItem(Sport sport, RestService restService) {
        DefaultCardItem cardItem = new DefaultCardItem(sport);
        cardItem.setTitle(sport.getTitle());
        cardItem.setDescription(sport.getTitle());
        cardItem.setImageUrl(restService.getCompleteUrlForExtension(sport.getImageUrlExtension()));

        return cardItem;
    }

    public static DefaultCardItem createDefaultCardItem(String title, String description, int resourceId) {
        DefaultCardItem cardItem = new DefaultCardItem(null);
        cardItem.setTitle(title);
        cardItem.setDescription(description);
        cardItem.setImageResourceId(resourceId);

        return cardItem;
    }
}

