package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by Berdsen on 20.10.2017.
 */

@Data
public class Text {

    @SerializedName("text")
    String text;

}
