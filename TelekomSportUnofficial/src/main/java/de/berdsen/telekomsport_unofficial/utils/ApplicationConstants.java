package de.berdsen.telekomsport_unofficial.utils;

/**
 * Created by Berdsen on 14.10.2017.
 */

public class ApplicationConstants {
    public static final String PREFERENCES_LOGIN_USERNAME = "__USERNAME__";
    public static final String PREFERENCES_LOGIN_PASSWORD = "__PASSWORD__";

    public static String getUserAgentValue() {
        String chrome_version = "Chrome/61.0.3163.100"; // chrome version
        String base = "Mozilla/5.0 ";
        base += "(Windows NT 10.0; Win64; x64) "; // windows version
        base += "AppleWebKit/537.36 (KHTML, like Gecko) ";
        base += "%CH_VER% Safari/537.36".replace("%CH_VER%", chrome_version);

        return base; //"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
    }

}
