package de.berdsen.telekomsport_unofficial.services;

import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.utils.ParseUtils;

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

        private final String ACCEPT_ENCODING = "Accept-Encoding";
        private final String ACCEPT_CHARSET = "Accept-Charset";
        private final String ENCODING_UTF8 = "UTF-8";
        private final String ENCODING_GZIP = "gzip";
        private final String USER_AGENT = "User-Agent";
        private final String COOKIE_HEADER_FIELD = "Set-Cookie";
        private final String USER_AGENT_VALUE = System.getProperty("http.agent");

        private final String INPUT_ELEMENT_ID = "login";
        private final String INPUT_TAG = "input";
        private final String ATTRIBUTE_NAME = "name";
        private final String ATTRIBUTE_VALUE = "value";
        private final String KEY_PASSWORD = "pw_pwd";
        private final String KEY_USERNAME = "pw_usr";
        private final String KEY_SUBMIT = "pw_submit";

        @Override
        protected Boolean doInBackground(Void... voids) {
            return executeLogin();
        }

        private HttpsURLConnection createConnection() throws IOException {
            URL url = new URL(constants.getLoginUrl());

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
            connection.setRequestProperty(ACCEPT_ENCODING, ENCODING_GZIP);
            connection.setRequestProperty(ACCEPT_CHARSET, ENCODING_UTF8);

            return connection;
        }

        private boolean executeLogin() {
            try {
                HttpsURLConnection connection = createConnection();

                // execute request and get response code
                int res = connection.getResponseCode();

                // we need the Session cookies
                Map<String, List<String>> headerFields = connection.getHeaderFields();
                List<String> cookies = headerFields.get(COOKIE_HEADER_FIELD);

                String html = "";

                if (res == HttpURLConnection.HTTP_OK) {

                    // we accepted gzip, so we need to extract it
                    InputStream in = new GZIPInputStream(connection.getInputStream());

                    html = ParseUtils.readHtmlFromInputStream(in, ENCODING_UTF8);

                } else {
                    // TODO: error handling
                    int error = res;
                }

                // parse html to document
                Document loginPage = Jsoup.parse(html);

                Element input = loginPage.getElementById(INPUT_ELEMENT_ID);

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

                if (!postParameter.containsKey(KEY_SUBMIT)) {
                    postParameter.put(KEY_SUBMIT, "");
                }

                ExecutePost(cookies, postParameter);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        private void ExecutePost(List<String> cookies, Map<String, String> postParameter) throws IOException {

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,String> param : postParameter.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), ENCODING_UTF8));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), ENCODING_UTF8));
            }
            String urlParameters = postData.toString();

            HttpsURLConnection connection = (HttpsURLConnection)(new URL(constants.getLoginEndpoint()).openConnection());
            connection.setRequestMethod("POST");

            if (cookies.size() > 0) {
                String[] value = cookies.get(0).split(";");
                connection.setRequestProperty("Cookie", value[0]);
            }

            connection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

            writer.write(urlParameters);
            writer.flush();

            String html = "";

            int res = connection.getResponseCode();

            if (res == HttpURLConnection.HTTP_OK) {
                html = ParseUtils.readHtmlFromInputStream(connection.getInputStream(), ENCODING_UTF8);
            } else {
                // TODO: error handling
                int error = res;
            }

            Document loginPage = Jsoup.parse(html);

            Elements title = loginPage.getElementsByTag("title");

            //TODO: save session cookies to some place

        }
    }




}
