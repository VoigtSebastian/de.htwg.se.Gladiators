#!/bin/bash
echo Pulling necessary docker images
docker pull mongo
docker pull adoptopenjdk/openjdk11:alpine-slim

echo Building Gladiators executable
sbt assembly

echo Removing old Dockerimages if they exist
docker image rm gladiators-main
docker image rm gladiators-player-service

echo Building Dockerimages
docker build ./Player -t gladiators-player-service
docker build . -t gladiators-main
