package ru.mail.teamcity.avatar.supplier.impl;

import jetbrains.buildServer.users.PropertyKey;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.SimplePropertyKey;
import org.jetbrains.annotations.NotNull;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 02.05.13 19:40
 */
public class DirectUrlAvatarSupplier implements AvatarSupplier {

  private final String PROPERTY_KEY_NAME = "avatar." + this.getClass().getSimpleName();
  private final PropertyKey PROPERTY_KEY = new SimplePropertyKey(PROPERTY_KEY_NAME);

  public String getAvatarUrl(SUser user) {
    return user.getPropertyValue(PROPERTY_KEY);
  }

  @NotNull
  public String getOptionName() {
    return "Direct url";
  }

  @NotNull
  public String getTemplate() {
    return "templates/directUrl.vm";
  }

  @NotNull
  public Map<String, String> getTemplateParams(@NotNull SUser user) {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("avatarUrl", getAvatarUrl(user));
    return params;
  }

  public void store(SUser user, Map<String, String[]> params) {
    String[] values = params.get("avatarUrl");
    if (null != values) {
      assert values.length > 0;
      user.setUserProperty(PROPERTY_KEY, values[0]);
    }
  }
}
