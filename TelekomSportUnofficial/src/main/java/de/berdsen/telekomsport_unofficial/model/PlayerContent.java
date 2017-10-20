package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by berthm on 20.10.2017.
 */

public class PlayerContent extends BaseContent {

    @SerializedName("data")
    List<VideoDetails> videoDetails;

}
