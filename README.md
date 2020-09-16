[![Build Status](https://travis-ci.org/VoigtSebastian/de.htwg.se.Gladiators.svg?branch=remaster)](https://travis-ci.org/VoigtSebastian/de.htwg.se.Gladiators) [![Coverage Status](https://coveralls.io/repos/github/VoigtSebastian/de.htwg.se.Gladiators/badge.svg?branch=remaster)](https://coveralls.io/github/VoigtSebastian/de.htwg.se.Gladiators?branch=remaster)

# Gladiators
## A turn-based game developed by Sascha Villing and Sebastian Voigt

This game is created as an assignment for the course 'Software Engineering' at the HTWG Konstanz.
The corresponding lecture is held by Prof. Marco Boger.

<p><img src="https://user-images.githubusercontent.com/43783342/82115339-85775180-9762-11ea-8e7c-070ec305ee9e.png" width="568"/></p>


## Docker Environment
Execute `docker-build.sh` to build the docker environment.

After building the environment, execute `docker-compose run --rm main` to run the docker services in interactive mode.

Installing docker-compose can be done by executing
 `curl -L "https://github.com/docker/compose/releases/download/1.25.5/docker-compose-$(uname -s)-$(uname -m)" -o ./local/bin/docker-compose`
 (The docker website wants you to install docker-compose as root, this way it's a per-user installation)
