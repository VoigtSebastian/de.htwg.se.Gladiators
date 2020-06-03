sbt assembly
docker build ./Player -t gladiators-player-service
docker build . -t gladiators-main
