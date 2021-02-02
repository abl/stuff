# Backend

This is a basic API backend built with Spark and Dagger.

## Nothing works / I see tons of errors

This project relies on Dagger; until you do a `./gradlew build` Dagger-generated
classes won't exist and many errors/problems will appear.

## Requirements

A recent Gradle (>= 6.1.1 but < 7.0.0) and JDK 11.

## Building

`./gradlew build`

## Testing

`./gradlew test`

## Running

`./gradlew run`

The server will start on port 5000 by default.

## Deploying to Heroku

To simulate a Heroku deployment locally, run these commands:

```
./gradlew stage
heroku local web
```

This performs a clean build and is therefore slower than `./gradlew run` but can
help debug issues where the backend works locally but fails on Heroku.

## Spotless?

Spotless automatically formats code. If it detects errors, run `./gradlew spotlessApply`
to automatically fix them. `./gradlew spotlessCheck` will run Spotless without applying
fixes.
