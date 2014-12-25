package ru.mail.teamcity.avatar.supplier.impl;

import com.timgroup.jgravatar.Gravatar;
import com.timgroup.jgravatar.GravatarDefaultImage;
import com.timgroup.jgravatar.GravatarRating;
import jetbrains.buildServer.users.SUser;
import org.jetbrains.annotations.NotNull;
import ru.mail.teamcity.avatar.supplier.AbstractAvatarSupplier;
import ru.mail.teamcity.avatar.supplier.IndividualAvatarSupplier;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 08.05.13 11:06
 */
public class GravatarAvatarSupplier extends AbstractAvatarSupplier implements IndividualAvatarSupplier {

  @NotNull
  private final Gravatar gravatar;

  public GravatarAvatarSupplier() {
    gravatar = new Gravatar();
    gravatar.setRating(GravatarRating.GENERAL_AUDIENCES);
    gravatar.setDefaultImage(GravatarDefaultImage.IDENTICON);
  }

  @NotNull
  public String getAvatarUrl(@NotNull SUser user) {
    String mail = user.getEmail();
    if (null != mail) {
      // TODO: check if avatar is found
      return gravatar.getUrl(mail);
    }
    return "";
  }

  @NotNull
  public String getOptionName() {
    return "Gravatar";
  }

  @NotNull
  public String getTemplate() {
    return "ru/mail/teamcity/avatar/templates/gravatar.vm";
  }

  @NotNull
  public Map<String, Object> getTemplateParams(@NotNull SUser user) {
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("avatarUrl", getAvatarUrl(user));
    return params;
  }

  public void store(SUser user, Map<String, String[]> params) {
  }
}
