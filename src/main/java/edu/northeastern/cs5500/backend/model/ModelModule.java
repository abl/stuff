package edu.northeastern.cs5500.backend.model;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {
    @Provides
    public Class<Stuff> provideStuffClass() {
        return Stuff.class;
    }

    @Provides
    public Class<User> provideUserClass() {
        return User.class;
    }
}
