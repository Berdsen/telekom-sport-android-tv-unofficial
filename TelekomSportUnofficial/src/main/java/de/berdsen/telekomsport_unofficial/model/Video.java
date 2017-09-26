package de.berdsen.telekomsport_unofficial.model;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by berthm on 26.09.2017.
 */

@Data
public class Video implements Serializable {
    private String title;
    private String description;
    private String videoUrl;
    private String category;
    private String poster;

    @Override
    public String toString() {
        return "Video {" +
                "title=\'" + title + "\'" +
                ", description=\'" + description + "\'" +
                ", videoUrl=\'" + videoUrl + "\'" +
                ", category=\'" + category + "\'" +
                ", poster=\'" + poster + "\'" +
                "}";
    }
}
