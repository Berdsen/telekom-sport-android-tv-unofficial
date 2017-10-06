package de.berdsen.telekomsport_unofficial.services;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import de.berdsen.telekomsport_unofficial.services.model.ExtendedCookieStore;
import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.services.interfaces.LoginFinishedHandler;
import de.berdsen.telekomsport_unofficial.services.AsyncTasks.LoginTask;

/**
 * Created by berthm on 05.10.2017.
 */

public class SessionService {

    private TelekomApiConstants constants;
    private CookieManager cookieManager;

    public SessionService(final TelekomApiConstants constants) {
        this.constants = constants;

        // see https://developer.android.com/reference/java/net/HttpURLConnection.html
        this.cookieManager = new CookieManager(new ExtendedCookieStore(), CookiePolicy.ACCEPT_ALL);
    }

    public void loginAsync() {
        loginAsync(null);
    }

    public void loginAsync(LoginFinishedHandler handler)  {
        if (isAuthenticated()) {
            executeLogout();
        }

        LoginTask lt = new LoginTask(constants, cookieManager);
        lt.loginFinished = handler;
        lt.execute();
    }

    private void executeLogout() {
        try {
            URI uri = new URI(constants.getBaseUrl());
            List<HttpCookie> httpCookies = this.cookieManager.getCookieStore().get(uri);

            for (HttpCookie c : httpCookies) {
                this.cookieManager.getCookieStore().remove(uri, c);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public boolean isAuthenticated() {
        try {
            URI uri = new URI(constants.getBaseUrl());
            List<HttpCookie> httpCookies = this.cookieManager.getCookieStore().get(uri);

            if (httpCookies.size() > 0) {
                return true;
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return false;
    }
}
