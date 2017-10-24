package de.berdsen.telekomsport_unofficial.services.AsyncTasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.model.VideoStreamData;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.services.interfaces.VideoUrlResolvedHandler;
import de.berdsen.telekomsport_unofficial.services.model.CookieJarImpl;
import de.berdsen.telekomsport_unofficial.utils.ApplicationConstants;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Berdsen on 24.10.2017.
 */

public class GetVideoUrlTask extends AsyncTask<Uri, Void, String> {

    private final SessionService sessionService;
    private final TelekomApiConstants constants;

    public VideoUrlResolvedHandler handler;

    public GetVideoUrlTask(SessionService sessionService, TelekomApiConstants constants) {
        this.sessionService = sessionService;
        this.constants = constants;
    }

    @Override
    protected String doInBackground(Uri... uris) {
        if (uris == null || uris.length == 0) {
            return null;
        }

        Uri mediaSourceUri = uris[0];

        //TODO: create Task from following source code
        String userAgent = ApplicationConstants.getUserAgentValue();
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl())
                .build();

        if (sessionService.isAuthenticated()) {
            List<HttpCookie> cookies = sessionService.getAuthCookies();
            List<Cookie> listOfCookies = new ArrayList<>();

            for (HttpCookie c : cookies) {
                Cookie parsedCookie = Cookie.parse(HttpUrl.parse(constants.getBaseUrl()), c.toString());
                listOfCookies.add(parsedCookie);
            }
            client.cookieJar().saveFromResponse(HttpUrl.parse(constants.getBaseUrl()), listOfCookies);
        }

        final String POST_MEDIA_TYPE = "application/x-www-form-urlencoded";

        RequestBody body = RequestBody.create(MediaType.parse(POST_MEDIA_TYPE), "");
        Request postVideo = new Request.Builder()
                .url(mediaSourceUri.toString())
                .addHeader("User-Agent", userAgent)
                .post(body)
                .build();

        try {
            Response response = client.newCall(postVideo).execute();

            String jsonData = response.body().string();
            VideoStreamData videoStreamData = new Gson().fromJson(jsonData, VideoStreamData.class);

            if ("success".equals(videoStreamData.getStatus())) {
                // TODO: cleanup
                return getUrlFromXml("https:" + videoStreamData.getStreamUrl().getUrls().get(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getUrlFromXml(String s) {
        String userAgent = ApplicationConstants.getUserAgentValue();
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl())
                .build();

        if (sessionService.isAuthenticated()) {
            List<HttpCookie> cookies = sessionService.getAuthCookies();
            List<Cookie> listOfCookies = new ArrayList<>();

            for (HttpCookie c : cookies) {
                Cookie parsedCookie = Cookie.parse(HttpUrl.parse(constants.getBaseUrl()), c.toString());
                listOfCookies.add(parsedCookie);
            }
            client.cookieJar().saveFromResponse(HttpUrl.parse(constants.getBaseUrl()), listOfCookies);
        }

        Request getXml = new Request.Builder()
                .url(s)
                .addHeader("User-Agent", userAgent)
                .build();

        try {
            Response response = client.newCall(getXml).execute();
            String xmlData = response.body().string();
            Document xmlDocument = Jsoup.parse(xmlData, "", Parser.xmlParser());


            Elements urlAttributes = xmlDocument.getElementsByAttribute("url");
            if (urlAttributes == null || urlAttributes.size() == 0) return null;

            Elements authAttributes = xmlDocument.getElementsByAttribute("auth");
            if (authAttributes == null || authAttributes.size() == 0) return null;

            String returnValue = "";

            returnValue += urlAttributes.get(0).attr("url");
            returnValue += "?hdnea=";
            returnValue += urlAttributes.get(0).attr("auth");

            return returnValue;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (handler == null) return;

        handler.resolvedVideoUrl(s);
    }
}
