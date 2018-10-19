package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by berthm on 15.10.2018.
 */

public class StatisticsContent extends BaseContent {

    @SerializedName("state")
    String state;

    @SerializedName("url")
    String urlExtension;

}
