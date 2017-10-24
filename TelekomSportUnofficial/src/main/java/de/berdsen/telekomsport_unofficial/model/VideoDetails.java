package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by berthm on 20.10.2017.
 */

@Data
public class VideoDetails {

    @SerializedName("videoID")
    int videoID;

    @SerializedName("title")
    String title;

    @SerializedName("description_bold")
    String descriptionBold;

    @SerializedName("description_regular")
    String descriptionRegular;

    @SerializedName("scheduled_start")
    Schedule scheduledStart;

    @SerializedName("pay")
    boolean pay;

    @SerializedName("ads")
    boolean ads;

    @SerializedName("islivestream")
    boolean islivestream;

    @SerializedName("images")
    EventImage images;
}
