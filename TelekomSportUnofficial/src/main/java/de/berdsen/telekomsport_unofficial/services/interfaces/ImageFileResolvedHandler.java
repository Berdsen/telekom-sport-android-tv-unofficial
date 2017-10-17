package de.berdsen.telekomsport_unofficial.services.interfaces;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by berthm on 17.10.2017.
 */

public interface ImageFileResolvedHandler {
    void imageFileResolved(File imageFile, Bitmap bitmap);
}
