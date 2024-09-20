#!/bin/bash

TIMESTAMP=$(date +"%Y%m%d%H%M%S")
GIT_BRANCH=$(git symbolic-ref --short HEAD)
docker build -t file.server:$GIT_BRANCH-$TIMESTAMP .
