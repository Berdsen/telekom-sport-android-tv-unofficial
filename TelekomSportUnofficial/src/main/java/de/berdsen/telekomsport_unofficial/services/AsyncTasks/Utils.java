package de.berdsen.telekomsport_unofficial.services.AsyncTasks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import de.berdsen.telekomsport_unofficial.model.BaseContent;
import de.berdsen.telekomsport_unofficial.model.BoxScoreContent;
import de.berdsen.telekomsport_unofficial.model.EventContent;
import de.berdsen.telekomsport_unofficial.model.NoVideoContent;
import de.berdsen.telekomsport_unofficial.model.PlayerContent;
import de.berdsen.telekomsport_unofficial.model.ResponseData;
import de.berdsen.telekomsport_unofficial.model.StatisticsContent;
import de.berdsen.telekomsport_unofficial.model.TextContent;
import de.berdsen.telekomsport_unofficial.model.VideoContent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Berdsen on 20.10.2017.
 */

public class Utils {

    private static Gson gsonInstance;

    public static Response safeLoadResponse(OkHttpClient client, Request requestToLoad) {
        try {
            return client.newCall(requestToLoad).execute();
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

            ResponseData<T> currentGenericData = getGsonInstance().fromJson(jsonData, type);

            return currentGenericData.getData();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Gson getGsonInstance() {
         return gsonInstance == null ? gsonInstance = new GsonBuilder().registerTypeAdapterFactory(new JsonAdapterFactory()).create() : gsonInstance;
    }

    private static final class JsonAdapterFactory extends ModelTypeAdapterFactory<BaseContent> {

        public JsonAdapterFactory() {
            super(BaseContent.class);
            registerSubtype(NoVideoContent.class, "noVideo"); // noVideo // content_id = 1
            registerSubtype(TextContent.class, "eventText"); // eventText // content_id = 2
            registerSubtype(VideoContent.class, "eventVideos"); // eventVideos // content_id = 3
            registerSubtype(EventContent.class, "relatedEvents"); // relatedEvents // content_id = 4
            registerSubtype(PlayerContent.class, "player"); // player // content_id = 5
            //registerSubtype(PlayerContent.class, "noVideoPost"); // noVideoPost // content_id = 6
            //registerSubtype(BaseContent.class, "eventLane"); // eventLane // content_id = 689
            registerSubtype(BoxScoreContent.class, "boxScore"); //boxScore // content_id = 1142
            registerSubtype(StatisticsContent.class, "statistics"); // statistics // content_id = 2138 || content_id = 2141
        }
    }
}

