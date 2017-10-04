package de.berdsen.telekomsport_unofficial.services;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
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
        CookieHandler.setDefault(this.cookieManager);
        this.cookieManager.getCookieStore().removeAll();
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
        //private final String USER_AGENT = "User-Agent";
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

            executeNewLogin();

            //return executeLogin();

            return false;
        }

        private class TestHttpClient
        {
            private List<String> cookies;
            private HttpsURLConnection conn;

            private String GetPageContent(String url) throws Exception {

                URL obj = new URL(url);
                conn = (HttpsURLConnection) obj.openConnection();

                // default is GET
                conn.setRequestMethod("GET");

                conn.setUseCaches(false);

                // act like a browser
                conn.setRequestProperty("User-Agent", USER_AGENT);
                conn.setRequestProperty("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                if (cookies != null) {
                    for (String cookie : this.cookies) {
                        conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
                    }
                }
                int responseCode = conn.getResponseCode();
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in =
                        new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Get the response cookies
                setCookies(conn.getHeaderFields().get("Set-Cookie"));

                return response.toString();

            }

            private void sendPost(String url, String postParams) throws Exception {

                URL obj = new URL(url);
                conn = (HttpsURLConnection) obj.openConnection();

                // Acts like a browser
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                //conn.setRequestProperty("Host", "accounts.google.com");
                conn.setRequestProperty("User-Agent", USER_AGENT);
                //conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                //conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                for (String cookie : this.cookies) {
                    conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
                }
                conn.setRequestProperty("Connection", "keep-alive");
                //conn.setRequestProperty("Referer", "https://accounts.google.com/ServiceLoginAuth");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

                conn.setDoOutput(true);
                conn.setDoInput(true);

                // Send post request
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();

                int responseCode = conn.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + postParams);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in =
                        new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // System.out.println(response.toString());

            }

            public String getFormParams(String html, String username, String password) throws UnsupportedEncodingException {

                System.out.println("Extracting form's data...");

                Document doc = Jsoup.parse(html);

                // Google form id
                Element loginform = doc.getElementById(INPUT_ELEMENT_ID);
                Elements inputElements = loginform.getElementsByTag(INPUT_TAG);
                List<String> paramList = new ArrayList<String>();
                for (Element inputElement : inputElements) {
                    String key = inputElement.attr(ATTRIBUTE_NAME);
                    String value = inputElement.attr(ATTRIBUTE_VALUE);

                    if (key.equals("pw_usr"))
                        value = username;
                    else if (key.equals("pw_pwd"))
                        value = password;
                    paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
                }

                // build parameters list
                StringBuilder result = new StringBuilder();
                for (String param : paramList) {
                    if (result.length() == 0) {
                        result.append(param);
                    } else {
                        result.append("&" + param);
                    }
                }
                return result.toString();
            }

            public List<String> getCookies() {
                return cookies;
            }

            public void setCookies(List<String> cookies) {
                this.cookies = cookies;
            }

        }

        private void executeNewLogin() {
            try {

                String pageUrl = "https://www.telekomsport.de";
                String loginUrl = "https://www.telekomsport.de/service/auth/web/login?headto=https://www.telekomsport.de/info";
                String loginEndpoint = "https://accounts.login.idm.telekom.com/sso";

                TestHttpClient client = new TestHttpClient();
                String page = client.GetPageContent(pageUrl);
                page = client.GetPageContent(loginUrl);
                String postParams = client.getFormParams(page, "", "");
                client.sendPost(loginEndpoint, postParams);
                String result = client.GetPageContent(pageUrl);

                if (result.toLowerCase().contains("logout")) {
                    // success
                }
                /*
                sendGet(constants.getLoginUrl());
                sendPost(constants.getLoginEndpoint(),
                        URLEncoder.encode("pw_usr", ENCODING_UTF8) + "=" + URLEncoder.encode("", ENCODING_UTF8) + "&" +
                        URLEncoder.encode("pw_pwd", ENCODING_UTF8) + "=" + URLEncoder.encode("", ENCODING_UTF8)
                );
                sendGet(constants.getBaseUrl());
                */
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        private final String USER_AGENT = "Mozilla/5.0";

        // HTTP GET request
        public void sendHttpGet(String url) throws Exception {

            HttpCookie cookie = new HttpCookie("lang", "en");

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            Log.d("sendGet", "\nSending 'GET' request to URL : " + url);


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            //print result
            System.out.println(response.toString());
            Log.d("Response Code", response.toString());
        }

        // HTTP POST request
        String  sendHttpPost(String url, String urlParams) throws Exception {

            HttpCookie cookie = new HttpCookie("lang", "en");

            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add request header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParams);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Response Code : " + response);
            return  response.toString();
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

                    loadResponseCookies(connection);

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

        List<HttpCookie> cookieList = new ArrayList<>();

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
                            cookieList.add(cookie);
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

            ArrayList<String> cs = new ArrayList<>();
            for (HttpCookie cookie : cookieList) {
                String c = cookie.getName() + "=" + cookie.getValue();
                cs.add(c);
            }

            conn.setRequestProperty("Cookie", TextUtils.join(";", cs));

            /*
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
            */
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

            loadResponseCookies(connection);

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
