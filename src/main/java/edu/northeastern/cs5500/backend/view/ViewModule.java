package edu.northeastern.cs5500.backend.view;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
public class ViewModule {
    @Provides
    @IntoSet
    public View provideStuffView(StuffView StuffView) {
        return StuffView;
    }

    @Provides
    @IntoSet
    public View provideStatusView(StatusView statusView) {
        return statusView;
    }

    @Provides
    @IntoSet
    public View provideUserView(UserView userView) {
        return userView;
    }
}
