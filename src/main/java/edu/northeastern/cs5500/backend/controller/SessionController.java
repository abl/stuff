package edu.northeastern.cs5500.backend.controller;

import edu.northeastern.cs5500.backend.model.Session;
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
public class SessionController {
    private final UserController userController;
    private final GenericRepository<Session> sessionRepository;

    @Inject
    public SessionController(
            UserController userController, GenericRepository<Session> sessionRepository) {
        this.userController = userController;
        this.sessionRepository = sessionRepository;

        log.info("SessionController > construct");
    }

    @Nullable
    Session createSession(@Nonnull String emailAddress, @Nonnull String password) {
        User user = userController.getUserByEmailAddress(emailAddress);
        if (user == null) return null;
        if (!user.getPassword().equals(password)) return null;

        // Creating a new session revokes all older sessions
        Collection<Session> oldSessions = sessionRepository.getAll();
        for (Session session : oldSessions) {
            if (session.getUserId().equals(user.getId())) sessionRepository.delete(session.getId());
        }

        // Create a new session for this user and return it
        // TODO: Implement session expiration?
        Session newSession = new Session();
        newSession.setUserId(user.getId());
        return sessionRepository.add(newSession);
    }

    @Nullable
    User getUserForSession(@Nonnull ObjectId sessionId) {
        Session session = sessionRepository.get(sessionId);

        if (session == null) return null;

        return userController.getUser(session.getUserId());
    }
}
