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
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.berdsen.telekomsport_unofficial.model.ResponseData;
import de.berdsen.telekomsport_unofficial.model.Sport;
import de.berdsen.telekomsport_unofficial.model.EpgData;
import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.services.interfaces.EpgResolvedHandler;
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
            return null;
        }

        currentSport = sports[0];

        List<String> eventLaneUrlExtensions = FindEventLaneUrls(currentSport);

        if (eventLaneUrlExtensions == null || eventLaneUrlExtensions.size() == 0) {
            return null;
        }
        return retrieveEpgData(currentSport, eventLaneUrlExtensions);
    }

    @Override
    protected void onPostExecute(List<EpgData> epgData) {
        if (handler == null) return;

        handler.epgDataResolved(currentSport, epgData);
    }

    private List<String> FindEventLaneUrls(Sport sport) {
        List<String> returnValue = new ArrayList<>();

        String html = ReadPlainHtml(sport.getPageUrl());

        if (html == null) {
            return returnValue;
        }

        Document sportsPageDocument = Jsoup.parse(html);

        if (sportsPageDocument == null) {
            return returnValue;
        }

        Elements elementsByAttribute = sportsPageDocument.body().getElementsByTag("event-lane");

        if (elementsByAttribute == null || elementsByAttribute.size() == 0) {
            return returnValue;
        }

        for (Element e : elementsByAttribute) {
            String urlExtension = e.attr("prop-url");
            if (urlExtension != null && urlExtension != "" && !returnValue.contains(urlExtension)) {
                returnValue.add(urlExtension);
            }
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

    private List<EpgData> retrieveEpgData(Sport sportToLoad, List<String> eventLaneUrlExtensions) {

        List<EpgData> returnValue = new ArrayList<>();

        String apiUrl = apiConstants.getBaseUrl() + apiConstants.getApiUrlExtension();

        OkHttpClient client = new OkHttpClient.Builder().build();

        for (String eventLaneExtension : eventLaneUrlExtensions) {
            Request request = new Request.Builder().url(apiUrl + eventLaneExtension).build();
            Response response = SafeLoadResponse(client, request);

            EpgData epgData = LoadEpgDataFromJson(response);
            if (epgData != null) {
                returnValue.add(epgData);
            }
        }

        return returnValue ;
    }

    private EpgData LoadEpgDataFromJson(Response response) {
        String jsonData = "";

        try {
            jsonData = response.body().string();
            Type genericType = new TypeToken<ResponseData<EpgData>>(){}.getType();
            ResponseData<EpgData> currentEpg = new Gson().fromJson(jsonData, genericType);

            return currentEpg.getData();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Response SafeLoadResponse(OkHttpClient client, Request requestToLoad) {
        try {
            return client.newCall(requestToLoad).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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

}
