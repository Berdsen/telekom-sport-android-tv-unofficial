[![Build Status](https://travis-ci.org/Berdsen/telekom-sport-android-tv-unofficial.svg?branch=master&style=flat-square)](https://travis-ci.org/Berdsen/telekom-sport-android-tv-unofficial)
[![GitHub release](https://img.shields.io/github/release/Berdsen/telekom-sport-android-tv-unofficial.svg?style=flat-square)]()
[![Github All Releases](https://img.shields.io/github/downloads/Berdsen/telekom-sport-android-tv-unofficial/total.svg?style=flat-square)]()
[![Maintainability](https://api.codeclimate.com/v1/badges/ef14274d082dfdbc002e/maintainability)](https://codeclimate.com/github/Berdsen/telekom-sport-android-tv-unofficial/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/ef14274d082dfdbc002e/test_coverage)](https://codeclimate.com/github/Berdsen/telekom-sport-android-tv-unofficial/test_coverage)
[![Issue Count](https://codeclimate.com/github/Berdsen/telekom-sport-android-tv-unofficial/badges/issue_count.svg)](https://codeclimate.com/github/Berdsen/telekom-sport-android-tv-unofficial)
[![license](https://img.shields.io/github/license/Berdsen/telekom-sport-android-tv-unofficial.svg?style=flat-square)]()

# Telekom Sport for AndroidTV - Unofficial #

This application will provide you access to the telekom sport streams on your AndroidTV.  
You can just watch videos you are eligible for with your account. 

## What is this repository for? ##

### Quick summary ###

Retrieving active streams from the Telekom Sport website and playing them, if you're eligible for.

### Version 0.7.0 ###

* embedded question before exit
* (hopefully) fixed FCBTv, at least for the Livestream 

### Version 0.6.0 ###

* set app status to beta
* added option to select video quality dependend on the available qualities of your network

### Version 0.5.1 ###

* Small fix to prevend crashing the app in Detailsscreen

### Version 0.5.0 ###

* more improvements in stability
* reworked the ImageCacheService
* displaying images seems to be no problem anymore
* removed unnecessary player controls like thumb up, etc.
* added quality action but without functionality for now
* forward / backward times are adjusted to thirty instead of ten seconds

### Version 0.4.0 ###

* some improvements in stability
* tried ImageCacheService for picture retrieval

### Version 0.3.0 ###

* first alpha release  
* login possible  
* watching live content possible  
* watching replay and game report possible

### Known Issues ###
* ~~sometimes pictures are not displayed~~
* some errors might have a wrong error message
* ~~player controls are not implemented completely~~
* after video finished the player screen keeps open
* ~~BayernTV is not playable at the moment~~

## How do I get set up? ##

### Deployment on your AndroidTV ###

You need a method to install apks on your AndroidTV.
Easiest way is to install ES File Manager through Google Play Store and afterwards attach an USB Stick with the downloaded APK
to the TV and execute the APK within the ES File Manager application.
Afterwards you should find a new app in your apps list.

### Disclaimer ###

This application is not official, so it might be, that it will get abondened at some time, or that the Telekom will provide an own application.  
Some of the API code is taken over from the kodi plugin, which you can find [here](https://github.com/asciidisco/plugin.video.telekom-sport)

Many thanks to asciidisco because the api is not very clear and obvious.

Also I haven't written an android app for a very long time, so this app is in a very early alpha stage, written in some spare time of mine.
So expect some bugs and create some issues in the issue tracker. I perhaps will fix them, as I got some time for that.

### Facing problems? ###

Use the issue tracker!
