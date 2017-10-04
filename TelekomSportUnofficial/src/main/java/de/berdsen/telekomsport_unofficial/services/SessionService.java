package de.berdsen.telekomsport_unofficial.services;

import android.os.AsyncTask;
import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
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
    private CookieManager cookieManager;

    public SessionService(TelekomApiConstants constants) {
        this.constants = constants;

        // see https://developer.android.com/reference/java/net/HttpURLConnection.html
        this.cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        cookieManager.getCookieStore().removeAll();
    }

    public void loginAsync() {
        LoginTask lt = new LoginTask(cookieManager);
        lt.execute();
    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String ACCEPT_ENCODING = "Accept-Encoding";
        private final String ACCEPT_CHARSET = "Accept-Charset";
        private final String ENCODING_UTF8 = "UTF-8";
        private final String ENCODING_GZIP = "gzip";
        private final String USER_AGENT = "User-Agent";
        private final String COOKIE_HEADER_FIELD = "Set-Cookie";
        private String USER_AGENT_VALUE = "";

        private final String INPUT_ELEMENT_ID = "login";
        private final String INPUT_TAG = "input";
        private final String ATTRIBUTE_NAME = "name";
        private final String ATTRIBUTE_VALUE = "value";
        private final String KEY_PASSWORD = "pw_pwd";
        private final String KEY_USERNAME = "pw_usr";
        private final String KEY_SUBMIT = "pw_submit";

        private CookieManager cookieManager;

        public LoginTask(CookieManager cookieManager) {
            this.cookieManager = cookieManager;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            setUserAgentValue();

            return executeLogin();
        }

        private void setUserAgentValue() {
            String chrome_version = "Chrome/59.0.3071.115";
            String base = "Mozilla/5.0 ";
            base += "(Windows NT 6.1; WOW64) ";
            base += "AppleWebKit/537.36 (KHTML, like Gecko) ";
            base += "%CH_VER% Safari/537.36".replace("%CH_VER%", chrome_version);

            USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
        }

        private HttpsURLConnection createConnection(String connectionUrl) throws IOException {
            URL url = new URL(connectionUrl);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            Map<String, List<String>> requestProperties = connection.getRequestProperties();

            connection.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
            connection.setRequestProperty(ACCEPT_ENCODING, ENCODING_GZIP);
            connection.setRequestProperty(ACCEPT_CHARSET, ENCODING_UTF8);

            //connection.setRequestProperty("User-agent", System.getProperty("http.agent"));

            connection.setUseCaches (false);

            return connection;
        }

        private boolean executeLogin() {
            try {
                if (checkLoggedIn()) return true;

                /*
                HttpsURLConnection firstConnection = createConnection("https://www.telekomsport.de");
                int firstRes = firstConnection.getResponseCode();
                Map<String, List<String>> headerFields1 = firstConnection.getHeaderFields();

                List<String> cookies = headerFields1.get(COOKIE_HEADER_FIELD);
                */



                HttpsURLConnection connection = createConnection(constants.getLoginUrl());

                // execute request and get response code
                int res = connection.getResponseCode();

                // we need the Session cookies
                Map<String, List<String>> headerFields = connection.getHeaderFields();
                List<String> cookies = headerFields.get(COOKIE_HEADER_FIELD);

                //loadResponseCookies(connection, this.cookieManager);

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

                // loadResponseCookies(connection, cookieManager);

                return checkLoggedIn();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        private boolean checkLoggedIn() throws IOException {
            HttpsURLConnection connection = createConnection(constants.getBaseUrl());
            populateCookieHeaders(connection);

            // execute request and get response code
            int res = connection.getResponseCode();

            if (res == HttpURLConnection.HTTP_OK) {
                // we accepted gzip, so we need to extract it
                InputStream in = new GZIPInputStream(connection.getInputStream());

                String html = ParseUtils.readHtmlFromInputStream(in, ENCODING_UTF8);

                // parse html to document
                Document pageContent = Jsoup.parse(html);

                Elements logout = pageContent.getElementsByClass("logout");

                if (logout != null && logout.size() > 0) {
                    return true;
                }
            }

            return false;
        }

        public void loadResponseCookies(HttpURLConnection conn) {

            //do nothing in case a null cokkie manager object is passed
            if (this.cookieManager == null || conn == null){
                return;
            }

            List<String> cookiesHeader = conn.getHeaderFields().get( COOKIE_HEADER_FIELD );
            if (cookiesHeader != null) {
                for (String cookieHeader : cookiesHeader) {
                    List<HttpCookie> cookies;
                    try {
                        cookies = HttpCookie.parse(cookieHeader);
                    } catch (NullPointerException e) {
                        continue;
                    }

                    if (cookies != null) {
                        for (HttpCookie cookie : cookies) {
                            // do something
                        }
                        if (cookies.size() > 0) {
                            try {
                                this.cookieManager.getCookieStore().add(new URI("http://www.telekomsport.de"), HttpCookie.parse(cookieHeader).get(0));
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        public void populateCookieHeaders(HttpURLConnection conn) {

            if (this.cookieManager != null) {
                //getting cookies(if any) and manually adding them to the request header
                List<HttpCookie> cookies = this.cookieManager.getCookieStore().getCookies();

                if (cookies != null) {
                    if (cookies.size() > 0) {
                        for (HttpCookie cookie : cookies) {
                        }

                        //adding the cookie header

                        conn.setRequestProperty("Cookie", TextUtils.join(";", cookies));
                    }
                }
            }
        }

        private void ExecutePost(List<String> cookies, Map<String, String> postParameter) throws IOException {

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,String> param : postParameter.entrySet()) {
                if (param.getValue() == "") continue;

                if (postData.length() != 0) postData.append('&');

                postData.append(URLEncoder.encode(param.getKey(), ENCODING_UTF8));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), ENCODING_UTF8));
            }
            String urlParameters = postData.toString();

            StringBuilder cookieData = new StringBuilder();
            for (String param : cookies) {

                if (cookieData.length() != 0) cookieData.append(";");
                String[] value = param.split(";");
                cookieData.append(value[0]);
                cookieData.append(value[1]);

            }
            String cookieParameters = cookieData.toString();

            urlParameters += "&headto=https://www.telekomsport.de/";// [{"key":"headto","value":"https://www.telekomsport.de/","description":""}]

            HttpsURLConnection connection = (HttpsURLConnection)(new URL(constants.getLoginEndpoint()).openConnection());
            connection.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
            connection.setRequestProperty(ACCEPT_ENCODING, ENCODING_GZIP);
            connection.setRequestProperty(ACCEPT_CHARSET, ENCODING_UTF8);
            // connection.setRequestProperty("Cookie", cookieParameters);

            populateCookieHeaders(connection);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            //connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream ( connection.getOutputStream () );
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            connection.connect();

            Map<String, List<String>> headerFields = connection.getHeaderFields();

            String html = "";

            int res = connection.getResponseCode();

            if (res == HttpURLConnection.HTTP_OK) {
                html = ParseUtils.readHtmlFromInputStream(new GZIPInputStream(connection.getInputStream()), ENCODING_UTF8);
            } else {
                // TODO: error handling
                int error = res;
            }

            Document loginPage = Jsoup.parse(html);

            Elements title = loginPage.getElementsByTag("title");

            loadResponseCookies(connection);
        }

        private void ExecutePost_Old(List<String> cookies, Map<String, String> postParameter) throws IOException {

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
