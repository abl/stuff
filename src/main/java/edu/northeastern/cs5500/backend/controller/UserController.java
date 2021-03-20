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
    }

    @Nullable
    public User getUser(@Nonnull ObjectId uuid) {
        log.info("UserController > getUser({})", uuid);
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

        user.setEmailAddress(user.getEmailAddress().toLowerCase());

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

    @Nullable
    /**
     * Attempt to get a user by emailAddress.
     *
     * @param emailAddress the emailAddress to match
     * @return a user if one matches; null if there is no match
     */
    public User getUserByEmailAddress(@Nonnull String emailAddress) {
        // TODO: This is very inefficient - a better approach would rely on extending repository
        Collection<User> users = getUser();

        for (User user : users) {
            if (user.getEmailAddress().equalsIgnoreCase(emailAddress)) {
                return user;
            }
        }
        return null;
    }
}
