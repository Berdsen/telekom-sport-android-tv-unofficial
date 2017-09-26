package de.berdsen.telekomsport_unofficial.dagger;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import de.berdsen.telekomsport_unofficial.AndroidApplication;

/**
 * Created by berthm on 26.09.2017.
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        ActivityBuilder.class,
        AppModule.class
})
public interface AppComponent {

    AndroidApplication getApplication();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder setApplication(AndroidApplication app);

        AppComponent build();

    }

    void inject(AndroidApplication app);
}
