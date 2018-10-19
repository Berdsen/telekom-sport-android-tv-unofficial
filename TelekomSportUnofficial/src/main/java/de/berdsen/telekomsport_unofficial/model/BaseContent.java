package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by berthm on 20.10.2017.
 */

@Data
public class BaseContent {

    @SerializedName("content_id")
    int contentId;

    @SerializedName("type")
    String type;

    @SerializedName("type_id")
    int typeId;

    @SerializedName("type_subdirectory")
    String typeSubdirectory;

    @SerializedName("title")
    String title;

    @SerializedName("data_url")
    String dataUrlExtension;
}


