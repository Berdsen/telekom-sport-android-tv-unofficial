package de.berdsen.telekomsport_unofficial.ui.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.app.DetailsSupportFragmentBackgroundController;
import android.widget.Toast;

import java.io.IOException;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.model.TelekomApiConstants;
import de.berdsen.telekomsport_unofficial.services.PicassoCache;

/**
 * Created by Berdsen on 28.09.2017.
 */

public abstract class AbstractBaseDetailsFragment extends DetailsSupportFragment {

    @Inject
    protected Context context;

    @Inject
    PicassoCache picassoCache;

    @Inject
    TelekomApiConstants constants;

    @Inject
    SharedPreferences sharedPreferences;

    private DetailsSupportFragmentBackgroundController backgroundController = new DetailsSupportFragmentBackgroundController(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    protected void loadBackgroundImage() {
        final Handler handler = new Handler(context.getMainLooper());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    final Bitmap bitmap = picassoCache.getPicassoCacheInstance().load(constants.getBannerImageUrl()).get();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getBackgroundController().enableParallax();
                                getBackgroundController().setCoverBitmap(bitmap);
                            } catch (Exception e) {
                                Toast.makeText(context, getString(R.string.selectedVideoDetails_backgroudImageError) + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }

    private DetailsSupportFragmentBackgroundController getBackgroundController() {
        return backgroundController;
    }
}
