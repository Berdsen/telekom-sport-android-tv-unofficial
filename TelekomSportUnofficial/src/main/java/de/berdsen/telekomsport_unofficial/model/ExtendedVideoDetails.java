package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Berdsen on 20.10.2017.
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ExtendedVideoDetails extends VideoDetails {

    @SerializedName("event-type")
    String eventType;

    @SerializedName("video_type")
    VideoType videoType;

    @SerializedName("active")
    boolean active;

}
