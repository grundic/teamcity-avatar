package ru.mail.teamcity.avatar.web;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.controllers.ActionErrors;
import jetbrains.buildServer.controllers.BaseFormXmlController;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.web.openapi.*;
import jetbrains.buildServer.web.util.SessionUser;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;
import ru.mail.teamcity.avatar.AppConfiguration;
import ru.mail.teamcity.avatar.service.AvatarService;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Grigory Chernyshev
 * Date: 29.04.13 21:39
 */
public class AvatarProfileController extends BaseFormXmlController {

  private final static Logger LOG = Logger.getInstance(AvatarProfileController.class.getName());

  @NotNull
  private final AvatarService myAvatarService;

  @NotNull
  private final String myJspPagePath;

  public AvatarProfileController(
          @NotNull AvatarService avatarService,
          @NotNull final PluginDescriptor pluginDescriptor,
          @NotNull final WebControllerManager webControllerManager,
          @NotNull final PagePlaces pagePlaces) {
    myAvatarService = avatarService;

    myJspPagePath = pluginDescriptor.getPluginResourcesPath("jsp/avatarProfile.jsp");
    webControllerManager.registerController("/avatarProfile.html", this);
    registerAvatarProfileTab(pluginDescriptor, pagePlaces);
  }

  private void registerAvatarProfileTab(@NotNull PluginDescriptor pluginDescriptor, @NotNull PagePlaces pagePlaces) {
    final SimpleCustomTab tab = new SimpleCustomTab(
            pagePlaces,
            PlaceId.MY_TOOLS_TABS,
            AppConfiguration.PLUGIN_NAME,
            pluginDescriptor.getPluginResourcesPath("jsp/avatarProfileTab.jsp"),
            "Avatar") {
    };

    tab.addJsFile(pluginDescriptor.getPluginResourcesPath("js/avatar-profile.js"));
    tab.register();
  }

  @Override
  protected ModelAndView doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
    final ModelAndView modelAndView = new ModelAndView(myJspPagePath);

    SUser user = SessionUser.getUser(request);
    AvatarSupplier avatarSupplier = myAvatarService.getAvatarSupplier(user);
    modelAndView.getModel().put("avatarService", myAvatarService);
    modelAndView.getModel().put("selectedAvatarSupplier", null != avatarSupplier ? avatarSupplier.getBeanName() : "");
    modelAndView.getModel().put("suppliers", myAvatarService.getSuppliers());
    return modelAndView;
  }

  @Override
  protected void doPost(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Element xmlResponse) {
    ActionErrors actionErrors = new ActionErrors();

    SUser user = SessionUser.getUser(request);
    String avatarSupplierKey = WebHelper.getAvatarSupplierKey(request);
    if (null == avatarSupplierKey) {
      actionErrors.addError("wrongKey", "Failed to get avatar supplier key from request!");
      writeErrors(xmlResponse, actionErrors);
      return;
    }

    AvatarSupplier avatarSupplier = myAvatarService.getAvatarSupplier(avatarSupplierKey);
    if (null == avatarSupplier) {
      actionErrors.addError("wrongSupplier", String.format("Failed to get avatar supplier by %s key!", avatarSupplierKey));
      writeErrors(xmlResponse, actionErrors);
      return;
    }

    myAvatarService.store(user, avatarSupplier, request.getParameterMap());
    xmlResponse.addContent("Avatar supplier successfully saved!");
  }
}
