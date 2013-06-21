Teamcity avatar
===============

Teamcity avatar is plugin for JetBrain's continues integration server, which adds avatar functionality.
Plugin comes with several avatar suppliers, which can get avatars from different places.

Features
--------
This plugin add additional tab in user's profile configuration.
Available avatar suppliers:
- Disabled: choosing this will turn off avatar for user.
- Direct url: get avatar from user provided url.
- Bundled avatars: user can select avatar from pre-packaged images.
- Gravatar service: get avatar from gravatar by user's email.
- Jira: if you have Atlassian Jira instance and users in Teamcity and Jira matches, this supplier will provide pictures
from Jira API.
- Tumblr: set avatar from Tumblr by username.

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

Configuration
-------------
Administrators can configure plugin a little bit. Configuration is located in admin area in Integrations sub section.
- *Enable automatic supplying* - if set, then plugin will try to get avatars from enabled suppliers, which do not
request configuration. For example, Gravatar, Jira and Tumblr are such suppliers. If user have already selected supplier
and it provision some picture, then it will be used. In other case, plugin will try each individual avatar supplier and
 first successfull will be used.
- Each supplier can be enabled or disabled. Disabled suppliers are supplied automatically and disabled in user profile.
Also administrator can drag'n'drop suppliers to change priority of automatic supplying.

Only Jira avatar supplier have to be configured individually. Jira doesn't provide anonymous access to it's API, so
administrator have to provide credentials for it. Config file is located in standard configuration folder on server
(usually, ~/.BuildServer/config/) and named *teamcity-avatar.properties*.
Here is example of it's configuration:

```
jira.server.url=http://localhost:2990/jira
jira.username=admin
jira.password=admin
```

Screenshots
-----------
![Screenshot1](https://github.com/grundic/teamcity-avatar/blob/master/screenshots/screen-1.png?raw=true)
![Screenshot2](https://github.com/grundic/teamcity-avatar/blob/master/screenshots/screen-2.png?raw=true)
![Screenshot3](https://github.com/grundic/teamcity-avatar/blob/master/screenshots/screen-3.png?raw=true)
![Screenshot4](https://github.com/grundic/teamcity-avatar/blob/master/screenshots/screen-4.png?raw=true)
