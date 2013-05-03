package ru.mail.teamcity.avatar.supplier;

import jetbrains.buildServer.users.SUser;

/**
 * User: Grigory Chernyshev
 * Date: 02.05.13 21:52
 */
public interface AvatarSupplier {
  /*
    Return url of avatar.
   */
  public String getAvatarUrl(SUser user);

  /*
   * Get html for configuration
   */
  public String getConfigView();

}
