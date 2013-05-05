package ru.mail.teamcity.avatar.supplier.impl;

import jetbrains.buildServer.users.SUser;
import org.jetbrains.annotations.NotNull;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 02.05.13 19:40
 */
public class DirectUrlAvatarSupplier implements AvatarSupplier {

  public String getAvatarUrl(SUser user) {
    return "DirectUrlAvatarSupplier";
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
}
