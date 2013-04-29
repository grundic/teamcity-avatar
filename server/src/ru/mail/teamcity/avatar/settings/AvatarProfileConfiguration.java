package ru.mail.teamcity.avatar.settings;

import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.web.openapi.*;
import jetbrains.buildServer.web.util.SessionUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.mail.teamcity.avatar.AppConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Grigory Chernyshev
 * Date: 29.04.13 21:39
 */
public class AvatarProfileConfiguration extends SimpleCustomTab {

  public AvatarProfileConfiguration(@NotNull PagePlaces pagePlaces,
                                    @NotNull WebControllerManager controllerManager,
                                    @NotNull PluginDescriptor descriptor,
                                    @NotNull final AvatarConfigurationService avatarConfigurationService) {

    super(
            pagePlaces,
            PlaceId.MY_TOOLS_TABS,
            AppConfiguration.PLUGIN_NAME,
            descriptor.getPluginResourcesPath("settings/avatarConfiguration.jsp"),
            "Avatar"
    );

    register();

    controllerManager.registerController("/profileAvatarConfig.html", new BaseController() {
      @Nullable
      @Override
      protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
        SUser user = SessionUser.getUser(request);

        avatarConfigurationService.setAvatarUrl(user, request.getParameter(AvatarConfigurationService.AVATAR_URL));
        return new ModelAndView(new RedirectView(String.format("profile.html?tab=%s", AppConfiguration.PLUGIN_NAME), true));
      }
    });
  }
}
