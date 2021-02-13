package edu.northeastern.cs5500.backend.controller;

import edu.northeastern.cs5500.backend.model.User;
import edu.northeastern.cs5500.backend.repository.GenericRepository;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class UserController {
    private final GenericRepository<User> userRepository;

    @Inject
    UserController(GenericRepository<User> userRepository) {
        this.userRepository = userRepository;

        log.info("UserController > construct");

        if (userRepository.count() > 0) {
            return;
        }

        log.info("UserController > construct > adding default user");

        final User defaultUser1 = new User();
        defaultUser1.setUsername("alice");

        final User defaultUser2 = new User();
        defaultUser2.setUsername("bob");
        defaultUser2.setEmailAddress("bob@fakedomain.nope");

        try {
            addUser(defaultUser1);
            addUser(defaultUser2);
        } catch (Exception e) {
            log.error("UserController > construct > adding default user > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public User getUser(@Nonnull ObjectId uuid) {
        log.debug("UserController > getUser({})", uuid);
        return userRepository.get(uuid);
    }

    @Nonnull
    public Collection<User> getUser() {
        log.debug("UserController > getUser()");
        return userRepository.getAll();
    }

    @Nonnull
    public User addUser(@Nonnull User user) throws Exception {
        log.debug("UserController > addUser(...)");
        if (!user.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidUserException");
        }

        ObjectId id = user.getId();

        if (id != null && userRepository.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return userRepository.add(user);
    }

    public void updateUser(@Nonnull User user) throws Exception {
        log.debug("UserController > updateUser(...)");
        userRepository.update(user);
    }

    public void deleteUser(@Nonnull ObjectId id) throws Exception {
        log.debug("UserController > deleteUser(...)");
        userRepository.delete(id);
    }
}
