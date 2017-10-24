package de.berdsen.telekomsport_unofficial.services.AsyncTasks;

import android.os.AsyncTask;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import de.berdsen.telekomsport_unofficial.model.BaseContent;
import de.berdsen.telekomsport_unofficial.model.EventContent;
import de.berdsen.telekomsport_unofficial.model.GameEvent;
import de.berdsen.telekomsport_unofficial.model.GameEventDetails;
import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.model.TextContent;
import de.berdsen.telekomsport_unofficial.model.VideoContent;
import de.berdsen.telekomsport_unofficial.services.interfaces.GameEventResolvedHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by berthm on 20.10.2017.
 */

public class LoadGameEventTask extends AsyncTask<GameEvent, Void, GameEventDetails> {

    private final TelekomApiConstants apiConstants;

    public GameEventResolvedHandler handler;
    private GameEvent currentGameEvent;

    public LoadGameEventTask(TelekomApiConstants constants) {
        this.apiConstants = constants;
    }

    @Override
    protected GameEventDetails doInBackground(GameEvent... gameEvents) {
        if (gameEvents == null || gameEvents.length == 0) {
            return null;
        }

        this.currentGameEvent = gameEvents[0];

        GameEventDetails resolvedEventDetails = resolveGameEvent();

        return resolvedEventDetails;
    }

    private GameEventDetails resolveGameEvent() {
        String url = apiConstants.getBaseUrl() + apiConstants.getApiUrlExtension() + currentGameEvent.getTarget();

        OkHttpClient client = new OkHttpClient.Builder().build();

        Request request = new Request.Builder().url(url).build();
        Response response = Utils.safeLoadResponse(client, request);

        GameEventDetails details = Utils.loadDataFromJson(response, GameEventDetails.class);

        return details;
    }

    @Override
    protected void onPostExecute(GameEventDetails eventDetails) {
        if (handler == null) return;

        handler.onGameEventResolved(currentGameEvent, eventDetails);
    }

}
