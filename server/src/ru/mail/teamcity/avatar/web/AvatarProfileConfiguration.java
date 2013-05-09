package ru.mail.teamcity.avatar.web;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.web.openapi.*;
import jetbrains.buildServer.web.util.SessionUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.mail.teamcity.avatar.AppConfiguration;
import ru.mail.teamcity.avatar.service.AvatarService;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 29.04.13 21:39
 */
public class AvatarProfileConfiguration extends SimpleCustomTab {

  private final static Logger LOG = Logger.getInstance(AvatarProfileConfiguration.class.getName());

  private final AvatarService myAvatarService;

  public AvatarProfileConfiguration(@NotNull PagePlaces pagePlaces,
                                    @NotNull WebControllerManager controllerManager,
                                    @NotNull PluginDescriptor descriptor,
                                    @NotNull AvatarService avatarService) {

    super(
            pagePlaces,
            PlaceId.MY_TOOLS_TABS,
            AppConfiguration.PLUGIN_NAME,
            descriptor.getPluginResourcesPath("jsp/avatarConfiguration.jsp"),
            "Avatar"
    );
    this.myAvatarService = avatarService;

    register();

    controllerManager.registerController("/profileAvatarConfig.html", new BaseController() {
      @Nullable
      @Override
      protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
        SUser user = SessionUser.getUser(request);
        String avatarSupplierKey = WebHelper.getAvatarSupplierKey(request);
        if (null == avatarSupplierKey) {
          LOG.error("Failed to get avatar supplier key!");
          return null;
        }

        AvatarSupplier avatarSupplier = myAvatarService.getAvatarSupplier(avatarSupplierKey);
        assert avatarSupplier != null;

        myAvatarService.store(user, avatarSupplier, request.getParameterMap());
        return new ModelAndView(new RedirectView(String.format("profile.html?tab=%s", AppConfiguration.PLUGIN_NAME), true));
      }
    });
  }

  @Override
  public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {
    super.fillModel(model, request);
    SUser user = SessionUser.getUser(request);
    AvatarSupplier avatarSupplier = myAvatarService.getAvatarSupplier(user);

    model.put("avatarService", myAvatarService);
    model.put("selectedAvatarSupplier", null != avatarSupplier ? avatarSupplier.getBeanName() : "");
    model.put("suppliers", myAvatarService.getSuppliers());
  }
}
