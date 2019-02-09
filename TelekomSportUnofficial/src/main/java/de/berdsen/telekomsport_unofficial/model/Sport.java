package de.berdsen.telekomsport_unofficial.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * Created by Berdsen on 27.09.2017.
 */

@Data
public final class Sport implements Parcelable {
    private final String title;
    private final String imageUrlExtension;
    private final String pageExtension;

    private String baseUrl;

    Sport(String title, String imageUrlExtension, String pageExtension, String epgExtension) {
        this.title = title;
        this.imageUrlExtension = imageUrlExtension;
        this.pageExtension = pageExtension;
    }

    protected Sport(Parcel in) {
        title = in.readString();
        imageUrlExtension = in.readString();
        pageExtension = in.readString();
        baseUrl = in.readString();
    }

    public String getImageUrl() {
        return buildUrl(imageUrlExtension);
    }

    public String getPageUrl() {
        return buildUrl(pageExtension);
    }

    private String buildUrl(String extension) {
        if (extension.toLowerCase().startsWith("http")) {
            // url seems to be rooted
            return extension;
        }

        return baseUrl + extension;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(imageUrlExtension);
        parcel.writeString(pageExtension);
        parcel.writeString(baseUrl);
    }

    public static final Creator<Sport> CREATOR = new Creator<Sport>() {
        @Override
        public Sport createFromParcel(Parcel in) {
            return new Sport(in);
        }

        @Override
        public Sport[] newArray(int size) {
            return new Sport[size];
        }
    };
}
