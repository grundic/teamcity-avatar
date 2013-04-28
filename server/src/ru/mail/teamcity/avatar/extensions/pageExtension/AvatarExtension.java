package ru.mail.teamcity.avatar.extensions.pageExtension;

import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimplePageExtension;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * User: Grigory Chernyshev
 * Date: 27.04.13 23:20
 */
public class AvatarExtension extends SimplePageExtension {

  public AvatarExtension(PagePlaces pagePlaces, PluginDescriptor descriptor) {
    super(pagePlaces, PlaceId.ALL_PAGES_FOOTER, "teamcity-avatar", descriptor.getPluginResourcesPath("extension.jsp"));
    addJsFile(descriptor.getPluginResourcesPath("js/avatar.js"));
    register();
  }


}
