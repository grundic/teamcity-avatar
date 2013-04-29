package ru.mail.teamcity.avatar.extensions.pageExtension;

import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimplePageExtension;

/**
 * User: Grigory Chernyshev
 * Date: 27.04.13 23:20
 */
public class JsInjectionExtension extends SimplePageExtension {

  public JsInjectionExtension(PagePlaces pagePlaces, PluginDescriptor descriptor) {
    super(pagePlaces, PlaceId.ALL_PAGES_FOOTER, "teamcity-avatar", descriptor.getPluginResourcesPath("extension.jsp"));
    addJsFile(descriptor.getPluginResourcesPath("js/avatar.js"));
    register();
  }


}
