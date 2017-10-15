package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by Berdsen on 15.10.2017.
 */

@Data
public class ResponseData<T> {

    @SerializedName("status")
    String status;

    @SerializedName("data")
    T data;
}
