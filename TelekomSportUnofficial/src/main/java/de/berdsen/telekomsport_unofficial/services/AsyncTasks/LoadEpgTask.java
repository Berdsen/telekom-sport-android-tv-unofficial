package de.berdsen.telekomsport_unofficial.services.AsyncTasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.berdsen.telekomsport_unofficial.model.EpgData;
import de.berdsen.telekomsport_unofficial.model.ResponseData;
import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.services.interfaces.EpgResolvedHandler;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Berdsen on 14.10.2017.
 */

public class LoadEpgTask extends AsyncTask<Sport, Void, List<EpgData>> {

    private final TelekomApiConstants apiConstants;

    public EpgResolvedHandler handler;
    private Sport currentSport;

    public LoadEpgTask(TelekomApiConstants apiConstants) {
        this.apiConstants = apiConstants;
    }

    @Override
    protected List<EpgData> doInBackground(Sport... sports) {

        if (sports == null || sports.length == 0) {
            return new ArrayList<>();
        }

        currentSport = sports[0];

        List<EventLaneData> eventLaneUrlExtensions = FindEventLaneUrls(currentSport);

        if (eventLaneUrlExtensions == null || eventLaneUrlExtensions.size() == 0) {
            return new ArrayList<>();
        }

        return retrieveEpgData(eventLaneUrlExtensions);
    }

    @Override
    protected void onPostExecute(List<EpgData> epgData) {
        if (handler == null) return;

        handler.epgDataResolved(currentSport, epgData);
    }

    private List<EventLaneData> FindEventLaneUrls(Sport sport) {
        List<EventLaneData> returnValue = new ArrayList<>();

        String html = ReadPlainHtml(sport.getPageUrl());

        if (html == null) {
            return returnValue;
        }

        Document sportsPageDocument = Jsoup.parse(html);

        if (sportsPageDocument == null) {
            return returnValue;
        }

        Elements contentGroups = sportsPageDocument.body().getElementsByClass("content-group");

        for (Element e : contentGroups) {
            Elements headline = e.getElementsByClass("headline"); // should be one element
            Elements eventLane = e.getElementsByTag("event-lane");

            if (headline == null || headline.size() == 0 || eventLane == null || eventLane.size() == 0) {
                continue;
            }

            String urlExtension = eventLane.get(0).attr("prop-url");

            EventLaneData eventLaneData = new EventLaneData();
            eventLaneData.setTitle(headline.get(0).text());
            eventLaneData.setEventLaneUrlExtension(urlExtension);

            returnValue.add(eventLaneData);
        }

        return returnValue;
    }

    private String ReadPlainHtml(String pageUrl) {
        try {
            String html = "";
            URL sportsUrl = new URL(pageUrl);
            BufferedReader in = new BufferedReader( new InputStreamReader( sportsUrl.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                html += inputLine;
            }
            in.close();

            return html;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<EpgData> retrieveEpgData(List<EventLaneData> eventLaneUrlExtensions) {

        List<EpgData> returnValue = new ArrayList<>();

        String apiUrl = apiConstants.getBaseUrl() + apiConstants.getApiUrlExtension();

        OkHttpClient client = new OkHttpClient.Builder().build();

        for (EventLaneData entry : eventLaneUrlExtensions) {
            Request request = new Request.Builder().url(apiUrl + entry.getEventLaneUrlExtension()).build();
            Response response = Utils.safeLoadResponse(client, request);

            EpgData epgData = Utils.loadDataFromJson(response, EpgData.class);

            if (epgData != null) {
                if (epgData.getTitle() == null || epgData.getTitle().length() == 0) {
                    epgData.setTitle(entry.getTitle());
                }
                returnValue.add(epgData);
            }
        }

        return returnValue;
    }

    private Document SafeParseDocument(Response response) {
        if (response == null || response.body() == null) {
            return null;
        }

        try {
            return Jsoup.parse( response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Data
    private class EventLaneData {
        String eventLaneUrlExtension;
        String title;
    }
}
