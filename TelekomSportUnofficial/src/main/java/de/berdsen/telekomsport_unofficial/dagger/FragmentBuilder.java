package de.berdsen.telekomsport_unofficial.dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import de.berdsen.telekomsport_unofficial.ui.fragments.SelectedVideoDetailsFragment;
import de.berdsen.telekomsport_unofficial.ui.fragments.SettingsFragment;
import de.berdsen.telekomsport_unofficial.ui.fragments.SportsOverviewFragment;
import de.berdsen.telekomsport_unofficial.ui.fragments.SportsVideoViewFragment;
import de.berdsen.telekomsport_unofficial.ui.fragments.VideoPlaybackFragment;

/**
 * Created by berthm on 26.09.2017.
 */

@Module
public abstract class FragmentBuilder {

    @ContributesAndroidInjector(modules = {
            ServicesModule.class
    })
    public abstract SportsOverviewFragment sportsOverviewFragment();

    @ContributesAndroidInjector(modules = {
            ServicesModule.class
    })
    public abstract SettingsFragment settingsFragment();

    @ContributesAndroidInjector(modules = {
            ServicesModule.class
    })
    public abstract SportsVideoViewFragment sportsVideoViewFragment();

    @ContributesAndroidInjector(modules = {
            ServicesModule.class
    })
    public abstract SelectedVideoDetailsFragment selectedVideoDetailsFragment();

    @ContributesAndroidInjector(modules = {
            ServicesModule.class
    })
    public abstract VideoPlaybackFragment videoPlaybackFragment();

}
