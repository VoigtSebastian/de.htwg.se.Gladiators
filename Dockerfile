FROM hseeberger/scala-sbt
WORKDIR /gladiators
ADD . /gladiators
CMD sbt run