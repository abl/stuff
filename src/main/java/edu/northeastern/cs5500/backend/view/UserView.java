package edu.northeastern.cs5500.backend.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.backend.JsonTransformer;
import edu.northeastern.cs5500.backend.controller.UserController;
import edu.northeastern.cs5500.backend.model.User;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import spark.Request;

@Singleton
@Slf4j
public class UserView implements View {

    @Inject
    UserView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject UserController userController;

    private User getUserFromRequest(Request request) {
        if (request.body() == null || request.body().isEmpty()) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = mapper.readValue(request.body(), User.class);
            return user;
        } catch (JsonParseException jpe) {
            log.info("getUserFromRequest > JsonParseException");
            return null;
        } catch (JsonMappingException jme) {
            log.info("getUserFromRequest > JsonMappingException");
            return null;
        } catch (JsonProcessingException jpe) {
            log.info("getUserFromRequest > JsonProcessingException");
            return null;
        }
    }

    @Override
    public void register() {
        log.info("UserView > register");

        get(
                "/user",
                (request, response) -> {
                    log.debug("/user");
                    response.type("application/json");
                    return userController.getUser();
                },
                jsonTransformer);

        get(
                "/user/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.info("/user/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    User user = userController.getUser(id);
                    if (user == null) {
                        log.info("userController gave us null for id '{}'", id);
                        halt(404);
                    }
                    response.type("application/json");
                    return user;
                },
                jsonTransformer);

        post(
                "/user",
                (request, response) -> {
                    User user = getUserFromRequest(request);
                    if (user == null) {
                        response.status(400);
                        return "";
                    }

                    // Ignore the user-provided ID if there is one
                    user.setId(null);
                    user = userController.addUser(user);

                    response.redirect(String.format("/user/{}", user.getId().toHexString()), 301);
                    return user;
                });

        put(
                "/user",
                (request, response) -> {
                    User user = getUserFromRequest(request);
                    if (user == null) {
                        response.status(400);
                        return "";
                    }

                    userController.updateUser(user);
                    return user;
                });

        delete(
                "/user",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    User user = mapper.readValue(request.body(), User.class);

                    userController.deleteUser(user.getId());
                    return user;
                });
    }
}
