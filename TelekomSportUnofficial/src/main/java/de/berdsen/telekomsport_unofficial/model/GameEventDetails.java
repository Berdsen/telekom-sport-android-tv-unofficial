package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by berthm on 20.10.2017.
 */

@Data
public class GameEventDetails {

    @SerializedName("metadata")
    GameEventMetadata metadata;

    @SerializedName("content")
    List<ContentGroup> contentGroups;

    public VideoDetails getLiveContent() {

        if (contentGroups == null) return null;

        for (ContentGroup cg : contentGroups) {
            for (BaseContent bc : cg.getContentEntries()) {
                if (!(bc instanceof PlayerContent)) continue;

                List<VideoDetails> vd = ((PlayerContent)bc).getVideoDetails();

                for (VideoDetails details : vd) {
                    if (details.isIslivestream())
                        return details;
                }

            }
        }

        return null;
    }

    public VideoDetails getSpecificVideoDetails(SpecifiedVideoType lookupVideoType) {
        if (contentGroups == null) return null;

        for (ContentGroup cg : contentGroups) {
            for (BaseContent bc : cg.getContentEntries()) {
                if (!(bc instanceof VideoContent)) continue;

                List<ExtendedVideoDetails> vd = ((VideoContent)bc).getVideoDetails();

                for (ExtendedVideoDetails details : vd) {
                    if (details.getVideoType().specifiedVideoType() == lookupVideoType)
                        return details;
                }
            }
        }

        return null;
    }
}
