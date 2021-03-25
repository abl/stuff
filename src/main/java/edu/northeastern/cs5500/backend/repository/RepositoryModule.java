package edu.northeastern.cs5500.backend.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.backend.model.Stuff;
import edu.northeastern.cs5500.backend.model.User;
import edu.northeastern.cs5500.backend.service.MongoDBService;

@Module
public class RepositoryModule {
    @Provides
    public GenericRepository<Stuff> provideStuffRepository() {
        return new InMemoryRepository<>();
    }

    @Provides
    public GenericRepository<User> provideUserRepository(MongoDBService mongoDBService) {
        return new MongoDBRepository<>(User.class, mongoDBService);
    }
}

/*
// Here's an example of how you imght swap out the in-memory repository for a database-backed
// repository:

package edu.northeastern.cs5500.backend.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.backend.model.Stuff;
import edu.northeastern.cs5500.backend.service.MongoDBService;

@Module
public class RepositoryModule {
    @Provides
    public GenericRepository<Stuff> provideStuffRepository(MongoDBService mongoDBService) {
        return new MongoDBRepository<>(Stuff.class, mongoDBService);
    }
}

*/
