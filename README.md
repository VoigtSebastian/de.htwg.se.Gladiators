[![Build Status](https://travis-ci.org/VoigtSebastian/de.htwg.se.Gladiators.svg?branch=master)](https://travis-ci.org/VoigtSebastian/de.htwg.se.Gladiators) [![Coverage Status](https://coveralls.io/repos/github/VoigtSebastian/de.htwg.se.Gladiators/badge.svg?branch=master)](https://coveralls.io/github/VoigtSebastian/de.htwg.se.Gladiators?branch=master)
# Gladiators
## A turn-based game developed by Sascha and Sebastian

This game is created as an assignment for the courses 'Software Engineering',
'Software Architectures' and 'Web Technology' at the HTWG Konstanz.
The corresponding lecture is held by Prof. Marco Boger.

This game has been rewritten from he ground up and is currently functionally
finished, but will get some upgrades in the future.

## Base game
This repository contains the
[base-game](https://github.com/VoigtSebastian/de.htwg.se.Gladiators) of
Gladiators with all of the logic and a GUI and TUI implementation to interact
with the game.

## Web-Frontend
In addition to the GUI and TUI implementation in this repository there is the
[web-frontend implementation](https://github.com/v1lling/de.htwg.wt.Gladiators)
which uses this repository as a library to provide logic and handling of json
parsing.

## Player-Service
To provide a persistent storage of user-data, we are currently developing a
[player-service API](https://github.com/VoigtSebastian/gladiators-player-service)
that handles registration, updating and in general, storage of Players.

### Todo
There are some things that are still on the list of features, that are
nice-to-have.

* [ ] A nicer TUI with better input and help-messages
* [ ] A nicer GUI with more graphics and more possibilities
* [ ] An undo-manager, to undo and redo commands
* [ ] A complete docker/Kubernetes integration to start database, player-service and web-frontend automatically
* [ ] CI/CD automation using GitHub-Actions
