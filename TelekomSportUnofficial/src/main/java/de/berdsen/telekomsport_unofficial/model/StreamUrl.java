package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by Berdsen on 24.10.2017.
 */

@Data
public class StreamUrl {

    @SerializedName("stream-access")
    List<String> urls;

}
