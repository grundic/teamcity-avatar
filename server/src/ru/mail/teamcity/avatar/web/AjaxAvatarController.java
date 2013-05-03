package ru.mail.teamcity.avatar.web;

import jetbrains.buildServer.controllers.AjaxRequestProcessor;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.UserModel;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.util.SessionUser;
import org.jdom.Element;
import org.jdom.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.ModelAndView;
import ru.mail.teamcity.avatar.service.AvatarConfigurationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * User: Grigory Chernyshev
 * Date: 30.04.13 12:43
 */
public class AjaxAvatarController extends BaseController {

  private final AvatarConfigurationService avatarConfigurationService;
  private final UserModel userModel;

  private final Pattern USER_EXTENDED_NAME = Pattern.compile("\\((\\w+)\\)$");

  public AjaxAvatarController(
          @NotNull WebControllerManager manager,
          @NotNull AvatarConfigurationService avatarConfigurationService,
          @NotNull UserModel userModel) {
    this.avatarConfigurationService = avatarConfigurationService;
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

          Map<String, String> settings = avatarConfigurationService.getSettings(user);

          for (String key : settings.keySet()) {
            final Element configElement = new Element(key);
            configElement.setContent(new Text(settings.get(key)));
            xmlResponse.addContent(configElement);
          }
        } catch (Exception e) {
          Loggers.SERVER.warn(e.getMessage());
          Loggers.SERVER.debug(e);
          final Element error = new Element("error");
          error.setContent(new Text(e.getMessage()));
        }
      }
    });

    return null;
  }

  private SUser getUser(String username) {
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
