package de.berdsen.telekomsport_unofficial.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import de.berdsen.telekomsport_unofficial.utils.ParseUtils;
import lombok.Data;

/**
 * Created by Berdsen on 24.10.2017.
 */

@Data
public class StreamUrl{

    private static final String IDENTIFIER = "stream-access";

    @SerializedName(IDENTIFIER)
    List<String> urls;

    public static StreamUrl parse(Object content) {
        StreamUrl returnValue = new StreamUrl();

        if (content != null && content instanceof LinkedTreeMap && ((LinkedTreeMap)content).containsKey(IDENTIFIER)) {
            returnValue.setUrls((ArrayList<String>)((LinkedTreeMap)content).get(IDENTIFIER));
        } else {
            returnValue.setUrls(new ArrayList<String>());

        }

        return returnValue;
    }
}
