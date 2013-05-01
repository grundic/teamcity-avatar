package ru.mail.teamcity.avatar.service;

import jetbrains.buildServer.users.PropertyKey;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.SimplePropertyKey;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 29.04.13 18:06
 */
public class AvatarConfigurationService {

  public static final String AVATAR_URL = "avatarUrl";
  private final PropertyKey AVATAR_URL_KEY = new SimplePropertyKey(AVATAR_URL);


  public String getAvatarUrl(SUser user){
    return user.getPropertyValue(AVATAR_URL_KEY);
  }

  public void setAvatarUrl(SUser user, String value){
    user.setUserProperty(AVATAR_URL_KEY, value);
  }

  public Map<String, String> getSettings(SUser user){
    Map<String, String> result = new HashMap<String, String>();
    result.put(AVATAR_URL, getAvatarUrl(user));

    return result;
  }
}
