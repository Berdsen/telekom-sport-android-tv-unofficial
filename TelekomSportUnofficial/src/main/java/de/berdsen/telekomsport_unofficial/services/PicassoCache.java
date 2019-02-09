package de.berdsen.telekomsport_unofficial.services;

import android.content.Context;

import com.squareup.picasso.Picasso;

/**
 * Created by Berdsen on 03.11.2017.
 */

public class PicassoCache {
    private Picasso picassoInstance = null;

    public PicassoCache(Context context) {
        picassoInstance = new Picasso.Builder(context).loggingEnabled(true).build();
    }

    public Picasso getPicassoCacheInstance() {
        return picassoInstance;
    }
}
