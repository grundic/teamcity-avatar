package ru.mail.teamcity.avatar.supplier.impl;

import jetbrains.buildServer.users.PropertyKey;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.SimplePropertyKey;
import org.jetbrains.annotations.NotNull;
import ru.mail.teamcity.avatar.supplier.AbstractAvatarSupplier;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 02.05.13 19:40
 */
public class DirectUrlAvatarSupplier extends AbstractAvatarSupplier {

  private final String PROPERTY_KEY_NAME = "avatar." + this.getClass().getSimpleName();
  private final PropertyKey PROPERTY_KEY = new SimplePropertyKey(PROPERTY_KEY_NAME);

  @NotNull
  public String getAvatarUrl(@NotNull SUser user) {
    String url = user.getPropertyValue(PROPERTY_KEY);
    return null == url ? "" : url;
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
  public Map<String, Object> getTemplateParams(@NotNull SUser user) {
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("avatarUrl", getAvatarUrl(user));
    return params;
  }

  public void store(@NotNull SUser user, @NotNull Map<String, String[]> params) {
    String[] values = params.get("avatarUrl");
    if (null != values) {
      assert values.length > 0;
      user.setUserProperty(PROPERTY_KEY, values[0]);
    }
  }
}
