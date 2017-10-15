package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by Berdsen on 14.10.2017.
 */

@Data
public class EpgData {

    @SerializedName("content_id")
    int contentId;

    @SerializedName("type")
    String type;

    @SerializedName("type_id")
    int typeId;

    @SerializedName("title")
    String title;

    @SerializedName("data_url")
    String dataUrl;

    @SerializedName("data_params")
    String dataParams;

    @SerializedName("data")
    List<GameEvent> events;
}
