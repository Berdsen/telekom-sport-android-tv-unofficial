package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by berthm on 20.10.2017.
 */

@Data
public class ContentGroup {

    @SerializedName("title")
    String title;

    @SerializedName("group_elements")
    List<BaseContent> contentEntries;
}
