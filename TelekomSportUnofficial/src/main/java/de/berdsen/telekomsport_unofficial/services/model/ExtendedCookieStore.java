package de.berdsen.telekomsport_unofficial.services.model;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by berthm on 06.10.2017.
 */

public class ExtendedCookieStore implements CookieStore {
    private final HashMap<URI, HashMap<String, HttpCookie>> cookieStore = new HashMap<>();

    @Override
    public void add(URI uri, HttpCookie httpCookie) {
        if (cookieStore.containsKey(uri)) {
            HashMap<String, HttpCookie> httpCookies = cookieStore.get(uri);
            httpCookies.put(httpCookie.getName(), httpCookie);
        } else {
            HashMap<String, HttpCookie> httpCookies = new HashMap<>();
            httpCookies.put(httpCookie.getName(), httpCookie);
            cookieStore.put(uri, httpCookies);
        }
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        List<HttpCookie> listOfCookies = new ArrayList<>();
        if (cookieStore.containsKey(uri)) {
            listOfCookies.addAll(cookieStore.get(uri).values());
        }

        return listOfCookies;
    }

    @Override
    public List<HttpCookie> getCookies() {
        List<HttpCookie> listOfCookies = new ArrayList<>();
        for (Map.Entry<URI, HashMap<String, HttpCookie>> uriCookies : cookieStore.entrySet() ){
            for (Map.Entry<String, HttpCookie> cookie : uriCookies.getValue().entrySet()) {
                listOfCookies.add(cookie.getValue());
            }
        }

        return listOfCookies;
    }

    @Override
    public List<URI> getURIs() {
        List<URI> listOfUris = new ArrayList<>();
        for (Map.Entry<URI, HashMap<String, HttpCookie>> uriCookies : cookieStore.entrySet() ){
            listOfUris.add(uriCookies.getKey());
        }
        return listOfUris;
    }

    @Override
    public boolean remove(URI uri, HttpCookie httpCookie) {
        if (cookieStore.containsKey(uri) && cookieStore.get(uri).containsKey(httpCookie.getName())) {
            if (cookieStore.get(uri).size() == 1) {
                cookieStore.remove(uri);
            } else {
                cookieStore.get(uri).remove(httpCookie.getName());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll() {
        cookieStore.clear();
        return true;
    }
}
