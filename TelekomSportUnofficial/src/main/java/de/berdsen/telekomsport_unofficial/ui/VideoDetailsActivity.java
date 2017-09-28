package de.berdsen.telekomsport_unofficial.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import de.berdsen.telekomsport_unofficial.R;
import de.berdsen.telekomsport_unofficial.ui.base.AbstractBaseActivity;

/**
 * Created by berthm on 28.09.2017.
 */

public class VideoDetailsActivity extends AbstractBaseActivity {

    public static final String EXTRA_VIDEO = "extra_video";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);
    }
}
