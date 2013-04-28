package ru.mail.teamcity.avatar.settings;

import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Grigory Chernyshev
 * Date: 28.04.13 17:01
 */
public class AvatarConfigurationController extends BaseController {

  private final WebControllerManager myWebControllerManager;
  private final PluginDescriptor pluginDescriptor;

  public AvatarConfigurationController(WebControllerManager webControllerManager, PluginDescriptor pluginDescriptor) {
    myWebControllerManager = webControllerManager;
    this.pluginDescriptor = pluginDescriptor;
  }

  @Nullable
  @Override
  protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
    return new ModelAndView(pluginDescriptor.getPluginResourcesPath("/profile.html?tab=teamcity-avatar"));
  }

  public void register() {
    myWebControllerManager.registerController("/avatar/config.html", this);
  }

}
