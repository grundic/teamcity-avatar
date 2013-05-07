package ru.mail.teamcity.avatar.supplier.impl;

import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mail.teamcity.avatar.supplier.AbstractAvatarSupplier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * \
 * User: Grigory Chernyshev
 * Date: 07.05.13 10:55
 */
public class BundledAvatarSupplier extends AbstractAvatarSupplier {

  private final String BUNDLED_AVATARS_PATH;

  public BundledAvatarSupplier(SBuildServer server, PluginDescriptor pluginDescriptor) {
    BUNDLED_AVATARS_PATH = server.getServerRootPath() + pluginDescriptor.getPluginResourcesPath() + "image/avatars";
  }

  @Nullable
  public String getAvatarUrl(SUser user) {

    return null;
  }

  @NotNull
  public String getOptionName() {
    return "Bundled avatars";
  }

  @NotNull
  public String getTemplate() {
    return "templates/bundled.vm";
  }

  @NotNull
  public Map<String, Object> getTemplateParams(@NotNull SUser user) {
    HashMap<String, Object> params = new HashMap<String, Object>();

    List<String> bundledAvatars = new ArrayList<String>();
    File avatarsDir = new File(BUNDLED_AVATARS_PATH);

    File[] files = avatarsDir.listFiles();
    if (null != files) {
      for (File avatar : files) {
        bundledAvatars.add(avatar.getName());
      }
    }

    params.put("bundledAvatars", bundledAvatars);
    return params;
  }

  public void store(SUser user, Map<String, String[]> params) {

  }
}
