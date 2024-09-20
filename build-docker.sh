#!/bin/bash

GIT_BRANCH=""

gradle wrapper

gradle clean test

GRADLE_EXIT_CODE=$?

if [ $GRADLE_EXIT_CODE -ne 0 ]; then
    exit $GRADLE_EXIT_CODE
fi

PROJECT_VER=$(./gradlew properties | grep -Po '(?<=version: ).*')

GIT_BRANCH=$(git symbolic-ref --short HEAD)
docker build --build-arg version=$PROJECT_VER -t file.server:$PROJECT_VER-$GIT_BRANCH .