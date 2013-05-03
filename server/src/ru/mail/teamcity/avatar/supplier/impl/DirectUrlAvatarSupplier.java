package ru.mail.teamcity.avatar.supplier.impl;

import jetbrains.buildServer.users.SUser;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;

/**
 * User: Grigory Chernyshev
 * Date: 02.05.13 19:40
 */
public class DirectUrlAvatarSupplier implements AvatarSupplier {

  public String getAvatarUrl(SUser user) {
    return "DirectUrlAvatarSupplier";
  }

  public String getConfigView() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
