package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by berthm on 20.10.2017.
 */

@Data
public class VideoType {
    @SerializedName("id")
    int id;

    @SerializedName("title")
    String title;

    @SerializedName("icon")
    String icon;
}