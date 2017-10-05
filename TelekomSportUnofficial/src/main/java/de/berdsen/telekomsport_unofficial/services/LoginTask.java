package de.berdsen.telekomsport_unofficial.services;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by berthm on 05.10.2017.
 */
class LoginTask extends AsyncTask<Void, Void, Boolean> {

    private final CookieManager cookieManager;
    private final TelekomApiConstants constants;

    private final String ENCODING_UTF8 = "UTF-8";
    private String USER_AGENT_VALUE = "";

    private final String INPUT_ELEMENT_ID = "login";
    private final String INPUT_TAG = "input";
    private final String ATTRIBUTE_NAME = "name";
    private final String ATTRIBUTE_VALUE = "value";
    private final String KEY_PASSWORD = "pw_pwd";
    private final String KEY_USERNAME = "pw_usr";
    private final String KEY_SUBMIT = "pw_submit";


    public LoginTask(TelekomApiConstants constants, CookieManager cookieManager) {
        this.cookieManager = cookieManager;
        this.constants = constants;
        setUserAgentValue();
    }

    private void setUserAgentValue() {
        String chrome_version = "Chrome/59.0.3071.115";
        String base = "Mozilla/5.0 ";
        base += "(Windows NT 6.1; WOW64) ";
        base += "AppleWebKit/537.36 (KHTML, like Gecko) ";
        base += "%CH_VER% Safari/537.36".replace("%CH_VER%", chrome_version);

        USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .cookieJar(new CookieJar() {

                        private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                        private HttpUrl getBaseUrl(HttpUrl url) {
                            return new HttpUrl.Builder()
                                    .scheme(url.scheme())
                                    .host(url.host())
                                    .build();
                        }

                        @Override
                        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                            List<Cookie> newCookies = new ArrayList<Cookie>();

                            for (Cookie c : cookies) {
                                if (c.value().toString().equalsIgnoreCase("deleted")) {
                                    Cookie e = c;
                                } else {
                                    newCookies.add(c);
                                }
                            }
                            cookieStore.put(getBaseUrl(url), newCookies);
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl url) {
                            List<Cookie> cookies = cookieStore.get(getBaseUrl(url));
                            return cookies != null ? cookies : new ArrayList<Cookie>();
                        }
                    })
                    .build();


            Request loginPage = new Request.Builder().url(constants.getLoginUrl()).build();
            Response response = client.newCall(loginPage).execute();

            Document loginPageDocument = Jsoup.parse(response.body().string());

            Element input = loginPageDocument.getElementById(INPUT_ELEMENT_ID);

            // get the requrired parameters
            Map<String, String> postParameter = new HashMap<>();

            if (input != null) {
                List<Element> listOfInputElements = input.getElementsByTag(INPUT_TAG);

                for (Element e : listOfInputElements) {
                    postParameter.put(e.attr(ATTRIBUTE_NAME), e.attr(ATTRIBUTE_VALUE));
                }
            }

            if (postParameter.containsKey(KEY_PASSWORD)) {
                postParameter.put(KEY_PASSWORD, "");
            }

            if (postParameter.containsKey(KEY_USERNAME)) {
                postParameter.put(KEY_USERNAME, "");
            }

            if (postParameter.containsKey("persist_session")) {
                // postParameter.put("persist_session", "1");
                postParameter.remove("persist_session");
            }

            if (!postParameter.containsKey(KEY_SUBMIT)) {
                postParameter.put(KEY_SUBMIT, "");
            }

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,String> param : postParameter.entrySet()) {
                if (postData.length() != 0) postData.append('&');

                postData.append(URLEncoder.encode(param.getKey(), ENCODING_UTF8));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), ENCODING_UTF8));
            }

            RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), postData.toString());
            Request login = new Request.Builder()
                    .url(constants.getLoginEndpoint())
                    .post(body)
                    .build();

            response = client.newCall(login).execute(); // do post and get cookies

            String html = response.body().string();

            Document loginTest = Jsoup.parse(html);

            Elements title = loginTest.getElementsByTag("title");

            if (title.text().toLowerCase().contains("sport")) {

                URI uri = new URI(constants.getBaseUrl());
                List<Cookie> storedCookies = client.cookieJar().loadForRequest(HttpUrl.get(uri));

                //TODO: put cookies into store
                //List<HttpCookie> httpCookies = this.cookieManager.getCookieStore().get(new URI(constants.getBaseUrl()));

                for (Cookie c : storedCookies) {
                    List<HttpCookie> parse = HttpCookie.parse(c.toString());

                    for (HttpCookie hc : parse) {
                        cookieManager.getCookieStore().add(uri, hc);
                    }
                }

                return true;
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return false;
    }
}

