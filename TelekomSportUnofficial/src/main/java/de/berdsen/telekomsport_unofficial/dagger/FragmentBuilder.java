package de.berdsen.telekomsport_unofficial.dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import de.berdsen.telekomsport_unofficial.ui.MainFragment;

/**
 * Created by berthm on 26.09.2017.
 */

@Module
public abstract class FragmentBuilder {

    @ContributesAndroidInjector
    public abstract MainFragment mainFragment();

}
