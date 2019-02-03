package de.berdsen.telekomsport_unofficial.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import dagger.Module;
import dagger.Provides;
import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.services.PicassoCache;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.services.SportsService;
import de.berdsen.telekomsport_unofficial.utils.ParseUtils;

/**
 * Created by berthm on 27.09.2017.
 */

@Module(includes = {
        AppModule.class
})
public class ServicesModule {

    private TelekomApiConstants singletonTelekomApiConstants;
    private RestService singletonRestService;
    private SessionService singletonSessionService;
    private PicassoCache singletonPicassoCache;
    private SportsService singletonSportsService;

    @Provides
    TelekomApiConstants providesTelekomConstants(Context context) {
        if (singletonTelekomApiConstants == null) {
            readTelekomConstants(context);
        }
        return singletonTelekomApiConstants;
    }

    @Provides
    RestService providesRestService(Context context, SessionService sessionService) {
        if (singletonRestService == null) {
            initializeRestService(context, sessionService);
        }
        return singletonRestService;
    }

    @Provides
    SessionService providesSessionService(Context context, SharedPreferences preferences) {
        if (singletonSessionService == null) {
            initializeSessionService(context, preferences);
        }
        return singletonSessionService;
    }

    @Provides
    PicassoCache providesPicassoCache(Context context) {
        if (singletonPicassoCache == null) {
            singletonPicassoCache = new PicassoCache(context);
        }
        return singletonPicassoCache;
    }

    @Provides
    SportsService providesSportsService() {
        if (singletonSportsService == null) {
            singletonSportsService = new SportsService();
        }
        return singletonSportsService;
    }

    private void readTelekomConstants(Context context) {
        String json = ParseUtils.loadJsonFromResource(context, R.raw.telekom_constants);

        Type type = new TypeToken<TelekomApiConstants>(){}.getType();

        Gson gson = new Gson();
        try {
            singletonTelekomApiConstants = gson.fromJson( json, type );
            singletonTelekomApiConstants.initBaseUrl();
        } catch (Exception e) {
            Log.e("MainFragment", e.getLocalizedMessage());
        }
    }

    private void initializeRestService(Context context, SessionService sessionService) {
        if (singletonTelekomApiConstants == null) {
            readTelekomConstants(context);
        }

        singletonRestService = new RestService(singletonTelekomApiConstants, sessionService);
    }

    private void initializeSessionService(Context context, SharedPreferences preferences) {
        if (singletonTelekomApiConstants == null) {
            readTelekomConstants(context);
        }

        singletonSessionService = new SessionService(singletonTelekomApiConstants, preferences);
    }
}
