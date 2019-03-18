package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Berdsen on 24.10.2017.
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class NoVideoContent extends BaseContent {

    //TODO: make this an extended GameEvent with videoId
    @SerializedName("data")
    GameEvent liveGameEvent;

}
