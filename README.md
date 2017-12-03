# Telekom Sport for AndroidTV - Unofficial #

This application will provide you access to the telekom sport streams on yout AndroidTV.  
You can just watch videos you are eligible for with your account. 

## What is this repository for? ##

### Quick summary ###

Retrieving active streams from the Telekom Sport website and playing them, if you're eligible for.

### Version 0.5.0 ###

> more improvements in stability
> reworked the ImageCacheService
> displaying images seems to be no problem anymore
> removed unnecessary player controls like thumb up, etc.
> added quality action but without functionality for now
> forward / backward times are adjusted to thirty instead of ten seconds

### Version 0.4.0 ###

> some improvements in stability
> tried ImageCacheService for picture retrieval

### Version 0.3.0 ###

> first alpha release  
> login possible  
> watching live content possible  
> watching replay and game report possible

### Known Issues ###
* ~~sometimes pictures are not displayed~~
* some errors might have a wrong error message
* player controls are not implemented completely
* after video finished the player screen keeps open

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

* Issue Tracker  
https://bitbucket.org/Berdsen/telekom-sport-android-tv-unofficial/issues?status=new&status=open

