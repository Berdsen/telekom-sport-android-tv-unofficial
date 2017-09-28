package de.berdsen.telekomsport_unofficial.dagger;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.services.RestService;
import de.berdsen.telekomsport_unofficial.services.SessionService;
import de.berdsen.telekomsport_unofficial.utils.JsonUtils;

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

    @Provides
    @Singleton
    TelekomApiConstants providesTelekomConstants(Context context) {
        if (singletonTelekomApiConstants == null) {
            readTelekomConstants(context);
        }
        return singletonTelekomApiConstants;
    }

    @Provides
    RestService providesRestService(Context context) {
        if (singletonRestService == null) {
            initializeRestService(context);
        }
        return singletonRestService;
    }

    @Provides
    SessionService providesSessionService(Context context) {
        if (singletonSessionService == null) {
            initializeSessionService(context);
        }
        return singletonSessionService;
    }

    private void readTelekomConstants(Context context) {
        String json = JsonUtils.loadJsonFromResource(context, R.raw.telekom_constants);

        Type type = new TypeToken<TelekomApiConstants>(){}.getType();

        Gson gson = new Gson();
        try {
            singletonTelekomApiConstants = gson.fromJson( json, type );
        } catch (Exception e) {
            Log.e("MainFragment", e.getLocalizedMessage());
        }
    }

    private void initializeRestService(Context context) {
        if (singletonTelekomApiConstants == null) {
            readTelekomConstants(context);
        }

        singletonRestService = new RestService(singletonTelekomApiConstants);
    }

    private void initializeSessionService(Context context) {
        if (singletonTelekomApiConstants == null) {
            readTelekomConstants(context);
        }

        singletonSessionService = new SessionService(singletonTelekomApiConstants);
    }
}
