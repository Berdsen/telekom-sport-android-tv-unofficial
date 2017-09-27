package de.berdsen.telekomsport_unofficial.dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import de.berdsen.telekomsport_unofficial.ui.OverviewActivity;

/**
 * Created by berthm on 26.09.2017.
 */

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {
            FragmentBuilder.class
    })
    public abstract OverviewActivity overviewActivity();

}
