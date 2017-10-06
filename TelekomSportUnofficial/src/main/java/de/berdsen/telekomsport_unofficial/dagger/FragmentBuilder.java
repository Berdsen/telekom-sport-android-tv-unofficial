package de.berdsen.telekomsport_unofficial.dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import de.berdsen.telekomsport_unofficial.ui.MainFragment;
import de.berdsen.telekomsport_unofficial.ui.VideoDetailsFragment;
import de.berdsen.telekomsport_unofficial.ui.sportsOverviewView.SportsOverviewFragment;

/**
 * Created by berthm on 26.09.2017.
 */

@Module
public abstract class FragmentBuilder {

    @ContributesAndroidInjector(modules = {
            ServicesModule.class
    })
    public abstract MainFragment mainFragment();

    @ContributesAndroidInjector(modules = {
            ServicesModule.class
    })
    public abstract VideoDetailsFragment videoDetailsFragment();

    @ContributesAndroidInjector(modules = {
            ServicesModule.class
    })
    public abstract SportsOverviewFragment sportsOverviewFragment();
}
