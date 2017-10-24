package de.berdsen.telekomsport_unofficial.services.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Berdsen on 24.10.2017.
 */

public class CookieJarImpl implements CookieJar {
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

}
