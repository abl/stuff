package edu.northeastern.cs5500.backend.view;

import static spark.Spark.halt;
import static spark.Spark.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.backend.JsonTransformer;
import edu.northeastern.cs5500.backend.controller.SessionController;
import edu.northeastern.cs5500.backend.model.Session;
import edu.northeastern.cs5500.backend.model.User;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class SessionView implements View {

    @Inject
    SessionView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject SessionController sessionController;

    @Override
    public void register() {
        log.info("SessionView > register");

        post(
                "/login",
                (request, response) -> {
                    log.debug("/login");
                    response.type("application/json");
                    ObjectMapper mapper = new ObjectMapper();
                    User user = mapper.readValue(request.body(), User.class);
                    if (!user.isValid()) halt(400);
                    Session session =
                            sessionController.createSession(
                                    user.getEmailAddress(), user.getPassword());
                    if (session == null) halt(401);
                    return session;
                },
                jsonTransformer);
    }
}
