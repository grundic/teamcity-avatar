package ru.mail.teamcity.avatar.supplier.impl;

import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.users.PropertyKey;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.SimplePropertyKey;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mail.teamcity.avatar.supplier.AbstractAvatarSupplier;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * \
 * User: Grigory Chernyshev
 * Date: 07.05.13 10:55
 */
public class BundledAvatarSupplier extends AbstractAvatarSupplier {

  private final String BUNDLED_AVATARS_PATH;

  private final String PROPERTY_KEY_NAME = "avatar." + this.getClass().getSimpleName();
  private final PropertyKey PROPERTY_KEY = new SimplePropertyKey(PROPERTY_KEY_NAME);

  public BundledAvatarSupplier(SBuildServer server, PluginDescriptor pluginDescriptor) {
    BUNDLED_AVATARS_PATH = server.getServerRootPath() + pluginDescriptor.getPluginResourcesPath() + "image/avatars";
  }

  @Nullable
  public String getAvatarUrl(SUser user) {
    //  TODO - remove hardcoded plugin URL. And check how it works in admin area.
    return "plugins/teamcity-avatar/image/avatars/" + user.getPropertyValue(PROPERTY_KEY);
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

    Map<String, String> bundledAvatars = new HashMap<String, String>();
    File avatarsDir = new File(BUNDLED_AVATARS_PATH);

    File[] files = avatarsDir.listFiles();
    if (null != files) {
      Arrays.sort(files);
      for (File avatar : files) {
        bundledAvatars.put(
                avatar.getName(),
                FilenameUtils.removeExtension(avatar.getName())
        );
      }
    }

    params.put("bundledAvatars", bundledAvatars);
    params.put("selectedAvatar", user.getPropertyValue(PROPERTY_KEY));
    return params;
  }

  public void store(SUser user, Map<String, String[]> params) {
    String[] values = params.get("bundledAvatar");
    if (null != values) {
      assert values.length > 0;
      user.setUserProperty(PROPERTY_KEY, values[0]);
    }
  }
}
