FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /gladiators-main
ADD target/scala-2.12/Gladiators.jar /gladiators-main
CMD java -jar Gladiators.jar
