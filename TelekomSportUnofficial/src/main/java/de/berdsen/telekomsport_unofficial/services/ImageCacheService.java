package de.berdsen.telekomsport_unofficial.services;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by berthm on 17.10.2017.
 */

public class ImageCacheService {

    private String directoryPath= "";
    private final String cachePathExtension = "/cache/images";

    public ImageCacheService(Context context) {
        try {
            directoryPath = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            directoryPath = context.getFilesDir().getAbsolutePath();
        }

        //TODO: scan over path and if files older than 5 days -> remove them
    }

    public File retrieveFileFromUrl(String url, Context context) {
        if (url == null || url.length() == 0 || !url.contains("/")) {
            return null;
        }

        final String imageName = url.substring(url.lastIndexOf("/") + 1);

        File directory = new File(getCacheImagePath());

        if (!directory.exists()) {
            directory.mkdir();
        }

        File[] foundFiles = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.contains(imageName);
            }
        });

        if (foundFiles.length > 0) {
            return foundFiles[0];
        }

        try {

            String filename = getCacheImagePath() + "/" + imageName;
            Bitmap bitmap = Picasso.with(context).load(url).get();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(filename));
            return new File(filename);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getCacheImagePath() {
        return directoryPath + cachePathExtension;
    }
}
