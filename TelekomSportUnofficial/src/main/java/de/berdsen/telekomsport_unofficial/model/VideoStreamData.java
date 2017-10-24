package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by Berdsen on 24.10.2017.
 */

@Data
public class VideoStreamData {
    @SerializedName("status")
    String status;

    @SerializedName("data")
    StreamUrl streamUrl;
}
