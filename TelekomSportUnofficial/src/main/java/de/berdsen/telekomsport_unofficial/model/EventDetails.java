package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by Berdsen on 15.10.2017.
 */

@Data
public class EventDetails {

    @SerializedName("home")
    Team home;

    @SerializedName("away")
    Team away;

}
