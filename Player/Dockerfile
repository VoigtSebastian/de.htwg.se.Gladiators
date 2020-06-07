FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /gladiators-player-service
ADD target/scala-2.12/Player-Service.jar /gladiators-player-service
EXPOSE 8081
CMD java -jar Player-Service.jar
