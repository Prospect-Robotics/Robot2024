# 2024 Robot Code

Gear Heads FRC 2813 Robot code for 2024 (Crescendo)

## Status

Main branch:
![main build succedded](https://github.com/Prospect-Robotics/Robot2024/actions/workflows/gradle.yml/badge.svg)

## One-time setup

### Install git

### Clone the repo

To clone the repositiory, run the following:


```
$ git clone --recurse-submodules https://github.com/Prospect-Robotics/Robot2024.git
```

> [!NOTE]
> Passing the https URL to `git clone` tends to work better than cloning using
> SSH, since the Prospect High School WiFi seems to block outgoing connections
> to github on the default SSH port.

## Development

### Building the code

The below instructions assume you are in the "Robot2024" subdirectory (i.e. the directory with "build.gradle").

To build the code and run the tests, run: `./gradlew build`

To start the simulator, run: `./gradlew simulateJava`

### Making changes

Do not commit changes directly to the master branch unless it is an emergency.
Instead, make changes on a branch, put the changes to GitHub, and request a
code review from @amrikverma or @cuttestkittensrule.

To create a new branch from the `master` branch, run:
`git checkout -b branch-name master`

> [!NOTE]
> It's common to prefix branch names with your github user name. For example
> if your GitHub user name was "chicken" you could run
> `git checkout -b chicken/throw-note-faster master` to create the branch.
