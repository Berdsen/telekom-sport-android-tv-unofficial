package de.berdsen.telekomsport_unofficial.ui.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import de.berdsen.telekomsport_unofficial.R;

public class AboutDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    private Context context;

    public AboutDescriptionPresenter(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object o) {
        String versionName = "0.0.0";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException nnfe) { /* ignore */ }

        String appName = context.getString(R.string.app_name);
        String appVersion = context.getString(R.string.app_version, versionName);
        String appInfo = context.getString(R.string.about_info);

        viewHolder.getTitle().setText(appName);
        viewHolder.getSubtitle().setText(appVersion);
        viewHolder.getBody().setText(appInfo);
    }
}
