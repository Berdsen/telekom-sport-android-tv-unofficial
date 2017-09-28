package de.berdsen.telekomsport_unofficial.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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

    public boolean login() {

        // load html and session stuff
        try {
            URL url = new URL(constants.getLoginUrl());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


}
