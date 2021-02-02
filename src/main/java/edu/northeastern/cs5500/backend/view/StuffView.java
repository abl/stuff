package edu.northeastern.cs5500.backend.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.backend.JsonTransformer;
import edu.northeastern.cs5500.backend.controller.StuffController;
import edu.northeastern.cs5500.backend.model.Stuff;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class StuffView implements View {

    @Inject
    StuffView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject StuffController StuffController;

    @Override
    public void register() {
        log.info("StuffView > register");

        get(
                "/stuff",
                (request, response) -> {
                    log.debug("/stuff");
                    response.type("application/json");
                    return StuffController.getStuff();
                },
                jsonTransformer);

        get(
                "/stuff/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/stuff/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    Stuff Stuff = StuffController.getStuff(id);
                    if (Stuff == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return Stuff;
                },
                jsonTransformer);

        post(
                "/stuff",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Stuff Stuff = mapper.readValue(request.body(), Stuff.class);
                    if (!Stuff.isValid()) {
                        response.status(400);
                        return "";
                    }

                    // Ignore the user-provided ID if there is one
                    Stuff.setId(null);
                    Stuff = StuffController.addStuff(Stuff);

                    response.redirect(String.format("/stuff/{}", Stuff.getId().toHexString()), 301);
                    return Stuff;
                });

        put(
                "/stuff",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Stuff Stuff = mapper.readValue(request.body(), Stuff.class);
                    if (!Stuff.isValid()) {
                        response.status(400);
                        return "";
                    }

                    StuffController.updateStuff(Stuff);
                    return Stuff;
                });

        delete(
                "/stuff",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Stuff Stuff = mapper.readValue(request.body(), Stuff.class);

                    StuffController.deleteStuff(Stuff.getId());
                    return Stuff;
                });
    }
}
