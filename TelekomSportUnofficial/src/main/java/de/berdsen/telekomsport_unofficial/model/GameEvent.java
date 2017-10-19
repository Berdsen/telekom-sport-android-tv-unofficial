package de.berdsen.telekomsport_unofficial.model;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by Berdsen on 15.10.2017.
 */

@Data
public class GameEvent {

    @SerializedName("type")
    String type;

    @SerializedName("target_type")
    String targetType;

    @SerializedName("target")
    String target;

    @SerializedName("target_url")
    String targetUrl;

    @SerializedName("target_playable")
    Boolean targetPlayable;

    @SerializedName("metadata")
    EventMetadata metadata;

    @SerializedName("blacklist_tags")
    List<String> blacklistTags;

    Drawable createdDrawable;
}
