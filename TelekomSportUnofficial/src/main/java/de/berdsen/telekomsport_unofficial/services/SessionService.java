package de.berdsen.telekomsport_unofficial.services;

import android.content.SharedPreferences;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.services.AsyncTasks.LoginTask;
import de.berdsen.telekomsport_unofficial.services.interfaces.LoginFinishedHandler;
import de.berdsen.telekomsport_unofficial.services.model.ExtendedCookieStore;
import de.berdsen.telekomsport_unofficial.services.model.LoginUserData;
import de.berdsen.telekomsport_unofficial.utils.ApplicationConstants;

/**
 * Created by berthm on 05.10.2017.
 */

public class SessionService {

    private TelekomApiConstants constants;
    private CookieManager cookieManager;
    private SharedPreferences sharedPreferences;

    public SessionService(final TelekomApiConstants constants, SharedPreferences sharedPreferences) {
        this.constants = constants;
        this.sharedPreferences = sharedPreferences;

        // see https://developer.android.com/reference/java/net/HttpURLConnection.html
        this.cookieManager = new CookieManager(new ExtendedCookieStore(), CookiePolicy.ACCEPT_ALL);
    }

    public void loginAsync(LoginFinishedHandler handler, boolean doLogout)  {
        if (isAuthenticated()) {
            if (doLogout) {
                executeLogout();
            } else {
                if (handler != null) handler.loginSucceeded();
                return;
            }
        }

        String username = sharedPreferences.getString(ApplicationConstants.PREFERENCES_LOGIN_USERNAME, "");
        String password = sharedPreferences.getString(ApplicationConstants.PREFERENCES_LOGIN_PASSWORD, "");

        if (username.length() == 0 || password.length() == 0) {
            if (handler != null) handler.loginFailed();
            return;
        }

        LoginTask lt = new LoginTask(constants, cookieManager);
        lt.loginFinished = handler;
        LoginUserData userData = new LoginUserData();

        userData.setUsername(username);
        userData.setPassword(password);

        lt.execute(userData);
    }

    public void executeLogout() {
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
                //TODO: check if cookies are valid
                return true;
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<HttpCookie> getAuthCookies() {
        try {
            URI uri = new URI(constants.getBaseUrl());
            return this.cookieManager.getCookieStore().get(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
}
