package de.berdsen.telekomsport_unofficial.services.AsyncTasks;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.services.interfaces.LoginFinishedHandler;
import de.berdsen.telekomsport_unofficial.services.model.CookieJarImpl;
import de.berdsen.telekomsport_unofficial.services.model.LoginUserData;
import de.berdsen.telekomsport_unofficial.utils.ApplicationConstants;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Berdsen on 05.10.2017.
 */
public class LoginTask extends AsyncTask<LoginUserData, Void, Boolean> {

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
    private final String KEY_PERSIST_SESSION = "persist_session";
    private final String KEY_SUBMIT = "pw_submit";
    private final String POST_MEDIA_TYPE = "application/x-www-form-urlencoded";

    public LoginFinishedHandler loginFinished = null;

    private final URI baseUri;

    public LoginTask(TelekomApiConstants constants, CookieManager cookieManager) {
        this.cookieManager = cookieManager;
        this.constants = constants;
        this.baseUri = URI.create(constants.getBaseUrl());

        setUserAgentValue();
    }

    private void setUserAgentValue() {
        USER_AGENT_VALUE = ApplicationConstants.getUserAgentValue();
    }

    @Override
    protected Boolean doInBackground(LoginUserData... userdataArray) {
        try {
            if (userdataArray == null || userdataArray.length < 1) {
                return false;
            }

            OkHttpClient client = new OkHttpClient.Builder()
                    .cookieJar(new CookieJarImpl())
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
                postParameter.put(KEY_PASSWORD, userdataArray[0].getPassword());
            }

            if (postParameter.containsKey(KEY_USERNAME)) {
                postParameter.put(KEY_USERNAME, userdataArray[0].getUsername());
            }

            // postParameter.put("persist_session", "1");
            postParameter.remove(KEY_PERSIST_SESSION);

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

            RequestBody body = RequestBody.create(MediaType.parse(POST_MEDIA_TYPE), postData.toString());
            Request login = new Request.Builder()
                    .url(constants.getLoginEndpoint())
                    .post(body)
                    .build();

            response = client.newCall(login).execute(); // do post and get cookies

            String html = response.body().string();

            Document loginTest = Jsoup.parse(html);

            Elements title = loginTest.getElementsByTag("title");

            if (title.text().toLowerCase().contains("sport")) {

                List<Cookie> storedCookies = client.cookieJar().loadForRequest(HttpUrl.get(baseUri));

                //TODO: put cookies into store
                //List<HttpCookie> httpCookies = this.cookieManager.getCookieStore().get(new URI(constants.getBaseUrl()));

                for (Cookie c : storedCookies) {
                    List<HttpCookie> parse = HttpCookie.parse(c.toString());

                    for (HttpCookie hc : parse) {
                        cookieManager.getCookieStore().add(baseUri, hc);
                    }
                }

                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean loginResult) {
        if (loginFinished == null) return;

        if (loginResult) {
            loginFinished.loginSucceeded();
        } else {
            loginFinished.loginFailed();
        }
    }
}

