package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by Berdsen on 20.10.2017.
 */

@Data
public class GameEventMetadata {

    @SerializedName("title")
    String title;

    @SerializedName("page_type")
    String pageType;

    @SerializedName("parent_title")
    String parentTitle;

    @SerializedName("web")
    Web web;

}
