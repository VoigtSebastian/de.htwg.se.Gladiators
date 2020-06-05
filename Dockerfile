FROM hseeberger/scala-sbt:8u222_1.3.5_2.13.1
WORKDIR /gladiators-main
ADD target/scala-2.12/Gladiators.jar /gladiators-main
CMD java -jar Gladiators.jar
