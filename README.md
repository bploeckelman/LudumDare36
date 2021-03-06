# Ludum Dare 36

[![Travis build status](https://travis-ci.org/bploeckelman/LudumDare36.svg)](https://travis-ci.org/bploeckelman/LudumDare36)
[![Download](https://api.bintray.com/packages/bploeckelman/LudumDare/LudumDare36/images/download.svg)](https://bintray.com/bploeckelman/LudumDare/LudumDare36/_latestVersion#files)

## Build Requirements

* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Android SDK (not studio)](https://developer.android.com/studio/index.html#downloads)

## Setup

### Mac OS X

The easy way to setup Mac OS X to do libGDX game dev is to utilize [homebrew](http://brew.sh)

Homebrew requires [xcode](https://developer.apple.com/xcode/downloads/).

Install Homebrew:

    /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

If you need to install `git` and clone the project, do that first:

    brew install git
    mkdir ~/code && cd ~/code
    git clone git@github.com:bploeckelman/LudumDare36.git
    cd LudumDare36

Install Build requirements:

    brew tap caskroom/cask
    brew cask install java
    brew install android-sdk
    echo yes | android update sdk -a --filter "platform-tools,build-tools-20.0.0,android-20" --no-ui --force
    echo "sdk.dir=/usr/local/opt/android-sdk" > local.properties

If you don't have a java IDE installed, you can easily download one
(IntelliJ in this example) with `brew cask`:

    brew cask install intellij-idea-ce

Eclipse and Netbeans are also available through `brew cask`.

**Other Tasks**

You might need to configure your `ANDROID_HOME` in your IDE to be
`/usr/local/opt/android-sdk`.

### Linux

Install `java` and `git` from your distro package manager.

Install Build requirements (if you don't have a global `android-sdk`
installed):

    cd LudumDare36
    ./travis/install-prerequisites.sh
    echo "sdk.dir=travis/android-sdk-linux" > local.properties

### Run the game!

    ./gradlew desktop:run

The game should build and run the desktop version.
