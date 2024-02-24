# 2024 Robot Code

Gear Heads FRC 2813 Robot code for 2024 (Crescendo)

## Status

Main branch:
![main build succedded](https://github.com/Prospect-Robotics/Robot2024/actions/workflows/gradle.yml/badge.svg)

## One-time setup

### Install git

### Clone the repo

Run the following:


```
$ git clone https://github.com/Prospect-Robotics/Robot2024.git
$ cd Robot2024/
$ git submodule update --init --recursive
```

Note that passing the https URL to `git clone` tends to work better with the
Prospect High School WiFi (the WiFi seems to block outgoing connections on the
default SSL port).

## Development

### Building the code

The below instructions assume you are in the "Robot2024" subdirectory (i.e. the directory with "build.gradle").

To build the code and run the tests, run: `./gradlew build`

To start the simulator, run: `./gradlew simulateJava`

### Making changes

Do not commit changes directly to the master branch unless it is an emergency.
Instead, make changes on a branch, put the changes to GitHub, and request a
code review.

To create a new branch from main, run: `git checkout -b branch-name master`

Note: It's common to prefix branch names with your github user name (for
example if your GitHub user name was "chicken" you could run
`git checkout -b chicken/throw-note-faster master`).