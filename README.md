# LibGDX Map Generator application
Nikita Abramenko <nikas31415@gmail.com>
v1.0, 2014-10-06

## Description
The project is designed for geographic map generation, for now. It will be able to do some more stuff in future,
for example, climate map. The code is based on LibGDX library, so in theory you can launch this on your mobile device.
But it's quite calculation heavy, i warned you :).

Map is generated via BFS algorithm running on high height points of a map. Most of the stuff is pseudo-randomly generated with
Gaussian distribution.

## Launch instruction
Open in you favourite Java IDE, change parameters in desktop/../DesktopLauncher and core/../GameLauncher,
then build jar via gradlew or launch DesktopLaucher from IDE.
