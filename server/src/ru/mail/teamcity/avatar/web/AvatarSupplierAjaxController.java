package ru.mail.teamcity.avatar.web;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.UserModel;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.util.SessionUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.ModelAndView;
import ru.mail.teamcity.avatar.service.AvatarService;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Grigory Chernyshev
 * Date: 03.05.13 12:38
 */
public class AvatarSupplierAjaxController extends AbstractAjaxController {

  private final static Logger LOG = Logger.getInstance(AvatarSupplierAjaxController.class.getName());

  private final AvatarService myAvatarService;
  private final UserModel userModel;

  public AvatarSupplierAjaxController(
          @NotNull WebControllerManager controllerManager,
          @NotNull PluginDescriptor pluginDescriptor,
          @NotNull SBuildServer server,
          @NotNull AvatarService avatarService,
          @NotNull UserModel userModel) throws Exception {
    super(pluginDescriptor, server);
    myAvatarService = avatarService;
    this.userModel = userModel;
    controllerManager.registerController("/avatarSupplierAjax.html", this);
  }

  @Nullable
  @Override
  protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
    SUser user = getUser(request);
    if (user == null) {
      LOG.warn("Failed to get user to return avatar for");
      return null;
    }

    String avatarSupplierKey = WebHelper.getAvatarSupplierKey(request);
    if (null == avatarSupplierKey) {
      LOG.warn("Failed to get avatar supplier key!");
      return null;
    }

    AvatarSupplier avatarSupplier = myAvatarService.getAvatarSupplier(avatarSupplierKey);
    if (null == avatarSupplier) {
      LOG.warn("Failed to get avatar supplier!");
      return null;
    }

    write(response, renderTemplate(avatarSupplier.getTemplate(), avatarSupplier.getTemplateParams(user)));
    return null;
  }

  private SUser getUser(HttpServletRequest request) {
    String username = request.getParameter("username");
    if (null == username) {
      return SessionUser.getUser(request);
    }
    return userModel.findUserAccount(null, username);
  }
}
