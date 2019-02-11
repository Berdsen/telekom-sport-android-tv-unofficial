package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Berdsen on 20.10.2017.
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class VideoContent extends BaseContent {

    @SerializedName("data")
    List<ExtendedVideoDetails> videoDetails;

}
