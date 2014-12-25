package ru.mail.teamcity.avatar.supplier.impl;

import jetbrains.buildServer.users.SUser;
import org.jetbrains.annotations.NotNull;
import ru.mail.teamcity.avatar.supplier.AbstractAvatarSupplier;

import java.util.Collections;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 03.05.13 23:07
 */
public class DisabledAvatarSupplier extends AbstractAvatarSupplier {
  @NotNull
  public String getAvatarUrl(SUser user) {
    return "";
  }

  @NotNull
  public String getOptionName() {
    return "Disabled";
  }

  @NotNull
  public String getTemplate() {
    return "ru/mail/teamcity/avatar/templates/disabled.vm";
  }

  @NotNull
  public Map<String, Object> getTemplateParams(@NotNull SUser user) {
    return Collections.emptyMap();
  }

  public void store(SUser user, Map<String, String[]> params) {
  }
}
