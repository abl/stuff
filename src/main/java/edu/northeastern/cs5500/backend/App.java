package edu.northeastern.cs5500.backend;

import static spark.Spark.*;

public class App {

    static int getAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 5000; // run on port 5000 by default
    }

    public static void main(String[] arg) {
        // run on port 5000
        port(getAssignedPort());

        // Allow all cross-origin requests
        // Don't do this for real projects!
        options(
                "/*",
                (request, response) -> {
                    String accessControlRequestHeaders =
                            request.headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header(
                                "Access-Control-Allow-Headers", accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod =
                            request.headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        // print all unhandled exceptions
        exception(Exception.class, (e, req, res) -> e.printStackTrace());

        // load and start the server
        DaggerServerComponent.create().server().start();
    }
}
