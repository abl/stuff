package edu.northeastern.cs5500.backend.controller;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class StatusController {

    @Inject
    StatusController() {
        log.info("StatusController > register");
    }

    @Nonnull
    public String getStatus() {
        return "OK";
    }
}
