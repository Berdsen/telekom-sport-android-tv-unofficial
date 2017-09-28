package de.berdsen.telekomsport_unofficial.services;

import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;

/**
 * Created by berthm on 28.09.2017.
 */

public class SessionService {

    private TelekomApiConstants constants;

    public SessionService(TelekomApiConstants constants) {
        this.constants = constants;
    }

    public void loginAsync() {

        LoginTask lt = new LoginTask();
        lt.execute();

    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return executeLogin();
        }

        private boolean executeLogin() {
            try {
                URL url = new URL(constants.getLoginUrl());

                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                //connection.setAllowUserInteraction(false);
                //connection.setInstanceFollowRedirects(true);
                //connection.setRequestMethod("GET");

                connection.setRequestProperty("User-Agent", System.getProperty("http.agent"));
                connection.setRequestProperty("Accept-Encoding", "gzip");

                // execute request and get response code
                int res = connection.getResponseCode();

                // we need the Session cookies
                List<String> cookies = connection.getHeaderFields().get("Set-Cookie");

                String html = "";

                if (res == HttpURLConnection.HTTP_OK) {
                    // we accepted gzip, so we need to extract it
                    InputStream in = new GZIPInputStream(connection.getInputStream());

                    // TODO: use encoding from html header
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);

                    // TODO: extract this to utils class
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    in.close();
                    html = sb.toString();
                } else {
                    // TODO: error handling
                    int error = res;
                }

                // parse html to document
                Document loginPage = Jsoup.parse(html);

                Element input = loginPage.getElementById("login");

                // get the requrired parameters
                Map<String, String> postParameter = new HashMap<>();

                if (input != null) {
                    List<Element> listOfInputElements = input.getElementsByTag("input");

                    for (Element e : listOfInputElements) {
                        postParameter.put(e.attr("name"), e.attr("value"));
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }




}
