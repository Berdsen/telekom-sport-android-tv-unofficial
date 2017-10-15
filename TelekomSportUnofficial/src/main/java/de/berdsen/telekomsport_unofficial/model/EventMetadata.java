package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by Berdsen on 15.10.2017.
 */

@Data
public class EventMetadata {

    @SerializedName("title")
    String title;

    @SerializedName("description_bold")
    String descriptionBold;

    @SerializedName("description_regular")
    String descriptionRegular;

    @SerializedName("scheduled_start")
    Schedule scheduleStart;

    @SerializedName("state")
    String state;

    @SerializedName("ppv")
    Boolean ppv;

    @SerializedName("images")
    EventImage images;

    @SerializedName("details")
    EventDetails details;

    @SerializedName("pay")
    Boolean pay;
}
