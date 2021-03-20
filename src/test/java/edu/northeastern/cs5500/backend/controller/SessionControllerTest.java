package edu.northeastern.cs5500.backend.controller;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.backend.model.Session;
import edu.northeastern.cs5500.backend.model.User;
import edu.northeastern.cs5500.backend.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;

public class SessionControllerTest {
    User createTestUser(String emailAddress, String password) {
        User user = new User();
        user.setEmailAddress(emailAddress);
        user.setPassword(password);
        return user;
    }

    @Test
    void testCanMakeSessionWithPassword() throws Exception {
        final String emailAddress = "a@b.com";
        final String password = "password";
        final String otherEmailAddress = "c@d.com";
        final String otherPassword = "otherPassword";
        UserController uC = new UserController(new InMemoryRepository<User>());
        User user = uC.addUser(createTestUser(emailAddress, password));
        uC.addUser(createTestUser(otherEmailAddress, otherPassword));

        SessionController sC = new SessionController(uC, new InMemoryRepository<Session>());

        Session session = sC.createSession(emailAddress, password);

        assertThat(session).isNotNull();
        assertThat(session.getId()).isNotNull();
        assertThat(session.getUserId()).isEqualTo(user.getId());
    }

    @Test
    void testSessionValidatesPassword() throws Exception {
        final String emailAddress = "a@b.com";
        final String password = "password";
        final String otherPassword = "otherPassword";
        UserController uC = new UserController(new InMemoryRepository<User>());
        User user = uC.addUser(createTestUser(emailAddress, password));

        SessionController sC = new SessionController(uC, new InMemoryRepository<Session>());

        Session session = sC.createSession(emailAddress, otherPassword);

        assertThat(session).isNull();
    }

    @Test
    void testCanGetUserFromSession() throws Exception {
        final String emailAddress = "a@b.com";
        final String password = "password";
        UserController uC = new UserController(new InMemoryRepository<User>());
        User user = uC.addUser(createTestUser(emailAddress, password));

        SessionController sC = new SessionController(uC, new InMemoryRepository<Session>());

        Session session = sC.createSession(emailAddress, password);

        User sessionUser = sC.getUserForSession(session.getId());

        assertThat(sessionUser).isNotNull();
        assertThat(sessionUser).isEqualTo(user);
    }

    @Test
    void testSessionValidatesId() throws Exception {
        final String emailAddress = "a@b.com";
        final String password = "password";
        UserController uC = new UserController(new InMemoryRepository<User>());
        User user = uC.addUser(createTestUser(emailAddress, password));

        SessionController sC = new SessionController(uC, new InMemoryRepository<Session>());

        sC.createSession(emailAddress, password);

        // Note that this is user.getId not session.getId!
        User sessionUser = sC.getUserForSession(user.getId());

        assertThat(sessionUser).isNull();
    }
}
