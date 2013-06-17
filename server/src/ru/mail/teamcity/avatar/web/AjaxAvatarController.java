package ru.mail.teamcity.avatar.web;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.controllers.AjaxRequestProcessor;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.UserModel;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.util.SessionUser;
import org.jdom.Element;
import org.jdom.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.ModelAndView;
import ru.mail.teamcity.avatar.service.AvatarService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * User: Grigory Chernyshev
 * Date: 30.04.13 12:43
 */
public class AjaxAvatarController extends BaseController {

  private final static Logger LOG = Logger.getInstance(AjaxAvatarController.class.getName());

  @NotNull
  private final AvatarService avatarService;
  @NotNull
  private final UserModel userModel;

  private final Pattern USER_EXTENDED_NAME = Pattern.compile("\\((\\w+)\\)$");

  public AjaxAvatarController(
          @NotNull WebControllerManager manager,
          @NotNull AvatarService avatarService,
          @NotNull UserModel userModel) {
    this.avatarService = avatarService;
    this.userModel = userModel;
    manager.registerController("/avatarAjax.html", this);
  }

  @Nullable
  @Override
  protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
    new AjaxRequestProcessor().processRequest(request, response, new AjaxRequestProcessor.RequestHandler() {
      public void handleRequest(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response, @NotNull final Element xmlResponse) {
        try {

          String username = request.getParameter("username");

          SUser user;
          if (null != username) {
            user = getUser(username);
          } else {
            user = SessionUser.getUser(request);
          }
          if (null == user) {
            return;
          }

          String avatarUrl = avatarService.getAvatarUrl(user);
          if (avatarUrl.startsWith("/")) {
            // Support relative avatar urls
            avatarUrl = request.getContextPath() + avatarUrl;
          }

          final Element configElement = new Element("avatarUrl");
          configElement.setContent(new Text(avatarUrl));
          xmlResponse.addContent(configElement);
        } catch (Exception e) {
          LOG.error(e.getMessage());
          final Element error = new Element("error");
          error.setContent(new Text(e.getMessage()));
        }
      }
    });

    return null;
  }

  @Nullable
  private SUser getUser(@NotNull String username) {
    SUser user = userModel.findUserAccount(null, username);
    if (null == user) {
      Matcher matcher = USER_EXTENDED_NAME.matcher(username);
      if (matcher.find()) {
        user = userModel.findUserAccount(null, matcher.group(1));
      }
    }

    return user;
  }
}
