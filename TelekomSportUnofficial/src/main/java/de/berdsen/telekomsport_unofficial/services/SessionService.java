package de.berdsen.telekomsport_unofficial.services;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;

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

        @Override
        protected Boolean doInBackground(Void... voids) {
            return executeLogin();
        }

        private boolean executeLogin() {
            // load html and session stuff
            try {
                URL url = new URL(constants.getLoginUrl());

                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                // we need the Session cookies
                List<String> cookies = connection.getHeaderFields().get("Set-Cookie");

                Map<String, List<String>> requestProperties = connection.getRequestProperties();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }




}
