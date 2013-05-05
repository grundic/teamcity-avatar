package ru.mail.teamcity.avatar.supplier.impl;

import jetbrains.buildServer.users.SUser;
import org.jetbrains.annotations.NotNull;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;

import java.util.Collections;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 03.05.13 23:07
 */
public class DisabledAvatarSupplier implements AvatarSupplier {
  public String getAvatarUrl(SUser user) {
    return null;
  }

  @NotNull
  public String getOptionName() {
    return "Disabled";
  }

  @NotNull
  public String getTemplate() {
    return "templates/disabled.vm";
  }

  @NotNull
  public Map<String, String> getTemplateParams(@NotNull SUser user) {
    return Collections.emptyMap();
  }
}
