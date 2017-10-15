package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by Berdsen on 15.10.2017.
 */

@Data
public class Team {

    @SerializedName("name_short")
    String nameShort;

    @SerializedName("name_full")
    String nameFull;

    @SerializedName("name_mini")
    String nameMini;

    @SerializedName("logo")
    String logo;
}
