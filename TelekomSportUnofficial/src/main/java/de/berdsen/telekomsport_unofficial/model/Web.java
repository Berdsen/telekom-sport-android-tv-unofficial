package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by Berdsen on 20.10.2017.
 */

@Data
public class Web {

    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("image")
    String imageUrlExtension;

    @SerializedName("canonical")
    String canonical;

}
