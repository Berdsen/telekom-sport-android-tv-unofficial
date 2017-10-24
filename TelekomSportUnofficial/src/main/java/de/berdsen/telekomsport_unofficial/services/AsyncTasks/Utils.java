package de.berdsen.telekomsport_unofficial.services.AsyncTasks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.io.IOException;
import java.lang.reflect.Type;

import de.berdsen.telekomsport_unofficial.model.BaseContent;
import de.berdsen.telekomsport_unofficial.model.EpgData;
import de.berdsen.telekomsport_unofficial.model.EventContent;
import de.berdsen.telekomsport_unofficial.model.GameEventDetails;
import de.berdsen.telekomsport_unofficial.model.NoVideoContent;
import de.berdsen.telekomsport_unofficial.model.PlayerContent;
import de.berdsen.telekomsport_unofficial.model.ResponseData;
import de.berdsen.telekomsport_unofficial.model.TextContent;
import de.berdsen.telekomsport_unofficial.model.VideoContent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by berthm on 20.10.2017.
 */

public class Utils {

    public static Response safeLoadResponse(OkHttpClient client, Request requestToLoad) {
        try {
            return client.newCall(requestToLoad).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static EpgData loadEpgDataFromJson(Response response) {
        String jsonData = "";

        try {
            jsonData = response.body().string();
            Type genericType = new TypeToken<ResponseData<EpgData>>(){}.getType();
            ResponseData<EpgData> currentGenericData = createGson().fromJson(jsonData, genericType);

            return currentGenericData.getData();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static GameEventDetails loadEventDetailsDataFromJson(Response response) {
        String jsonData = "";

        try {
            jsonData = response.body().string();
            Type genericType = new TypeToken<ResponseData<GameEventDetails>>(){}.getType();

            ResponseData<GameEventDetails> currentGenericData = createGson().fromJson(jsonData, genericType);

            return currentGenericData.getData();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T loadDataFromJson(Response response, Type classType) {
        String jsonData = "";

        try {
            jsonData = response.body().string();

            Type type = TypeToken.getParameterized(ResponseData.class, classType).getType();

            // Type genericType = new TypeToken<ResponseData<T>>(){}.getType();

            ResponseData<T> currentGenericData = createGson().fromJson(jsonData, type);

            return currentGenericData.getData();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new JsonAdapterFactory())
                .create();
    }
    private static final class JsonAdapterFactory extends RuntimeTypeAdapterFactory<BaseContent> {

        public JsonAdapterFactory() {
            super(BaseContent.class, "content_id");
            registerSubtype(NoVideoContent.class, "1");
            registerSubtype(TextContent.class, "2");
            registerSubtype(VideoContent.class, "3");
            registerSubtype(EventContent.class, "4");
            registerSubtype(PlayerContent.class, "5");
        }
    }

}
