package ru.mail.teamcity.avatar.supplier;

import jetbrains.buildServer.users.SUser;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 02.05.13 21:52
 */
public interface AvatarSupplier {

  /*
   * Get configured name for Bean instance
   */
  @NotNull
  public String getBeanName();

  /*
    Return url of avatar from Teamcity user.
   */
  @NotNull
  public String getAvatarUrl(SUser user);

  /*
    Return url of avatar from some identifier.
    This method is useful for users not registered in Teamcity, but presenting in VCS changes list.
    Not all suppliers can support this functionality.
   */
  @NotNull
  public String getAvatarUrl(String identifier);

  /*
   * Return name of option for configuration
   */
  @NotNull
  public String getOptionName();

  /*
   * Return velocity template path for configuring supplier
   */
  @NotNull
  public String getTemplate();

  /*
   * Parameters for velocity template
   */
  @NotNull
  public Map<String, Object> getTemplateParams(@NotNull SUser user);

  /*
   * Store user data
   */
  public void store(SUser user, Map<String, String[]> params);


}
