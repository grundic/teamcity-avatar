package ru.mail.teamcity.avatar.web;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.util.SessionUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.ModelAndView;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Grigory Chernyshev
 * Date: 03.05.13 12:38
 */
public class AvatarSupplierAjaxController extends AbstractAjaxController {

  private final static Logger LOG = Logger.getInstance(AvatarSupplierAjaxController.class.getName());

  public AvatarSupplierAjaxController(
          @NotNull WebControllerManager controllerManager,
          @NotNull PluginDescriptor pluginDescriptor,
          @NotNull SBuildServer server) throws Exception {
    super(pluginDescriptor, server);
    controllerManager.registerController("/avatarSupplierAjax.html", this);
  }

  @Nullable
  @Override
  protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
    SUser user = getUser(request);
    if (user == null) {
      return null;
    }

    AvatarSupplier avatarSupplier = WebHelper.getAvatarSupplier(request);
    if (null == avatarSupplier) {
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

    //TODO: get user by username
    return null;
  }

}
