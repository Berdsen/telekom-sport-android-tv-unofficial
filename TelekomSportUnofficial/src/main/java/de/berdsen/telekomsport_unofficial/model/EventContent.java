package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Berdsen on 20.10.2017.
 */

public class EventContent extends BaseContent {

    @SerializedName("data")
    List<GameEvent> gameEvents;
}
