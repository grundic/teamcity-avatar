Teamcity avatar
===============

Teamcity avatar is plugin for JetBrain's continues integration server, which adds avatar functionality.

Features
--------
This plugin add additional tab in user's profile.
Available options:
- Direct url: get avatar from http source
- Bundled avatars: user can select avatar from pre-packaged images
- Gravatar service: get avatar from gravatar by user's email

After selecting avatar, it's image will appear near to user name in top left corner.
Also, avatar will be displayed in pending changes for each user with configured avatar.

Important note: user must have *View user profile* permission in order to view avatars of other people. You can add
this permission in Administration area in Roles tab.

Installation
------------
Guide from Teamcity's documentation:
* Shutdown TeamCity server
* Copy the zip archive with the plugin into <TeamCity Data Directory>/plugins directory (usually ~/.BuildServer/plugins)
* Start TeamCity server: the plugin files will be unpacked and processed automatically.

Screenshots
-----------
![Screenshot1](https://github.com/grundic/teamcity-avatar/blob/master/screenshots/screen-1.png?raw=true)
![Screenshot2](https://github.com/grundic/teamcity-avatar/blob/master/screenshots/screen-2.png?raw=true)
![Screenshot3](https://github.com/grundic/teamcity-avatar/blob/master/screenshots/screen-3.png?raw=true)
