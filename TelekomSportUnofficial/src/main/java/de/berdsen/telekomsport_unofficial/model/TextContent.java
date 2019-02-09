package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by Berdsen on 20.10.2017.
 */

@Data
public class TextContent extends BaseContent {

    @SerializedName("data")
    Text text;

}
