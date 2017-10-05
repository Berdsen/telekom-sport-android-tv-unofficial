package de.berdsen.telekomsport_unofficial.services;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;

/**
 * Created by berthm on 05.10.2017.
 */

public class SessionService {

    private TelekomApiConstants constants;
    private CookieManager cookieManager;

    public SessionService(TelekomApiConstants constants) {
        this.constants = constants;

        // see https://developer.android.com/reference/java/net/HttpURLConnection.html
        this.cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(this.cookieManager);
    }

    public void loginAsync() {
        LoginTask lt = new LoginTask(constants, cookieManager);
        lt.execute();
    }
}
