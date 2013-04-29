package ru.mail.teamcity.avatar.settings;

import jetbrains.buildServer.users.PropertyKey;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.SimplePropertyKey;

/**
 * User: Grigory Chernyshev
 * Date: 29.04.13 18:06
 */
public class AvatarConfigurationService {

  public static final String AVATAR_URL = "avatarUrl";
  private PropertyKey AVATAR_URL_KEY = new SimplePropertyKey(AVATAR_URL);


  public String getAvatarUrl(SUser user){
    return user.getPropertyValue(AVATAR_URL_KEY);
  }

  public void setAvatarUrl(SUser user, String value){
    user.setUserProperty(AVATAR_URL_KEY, value);
  }
}
