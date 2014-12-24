package ru.mail.teamcity.avatar.web;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.ServiceLocator;
import jetbrains.buildServer.controllers.AjaxRequestProcessor;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.users.InvalidUsernameException;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.UserModel;
import jetbrains.buildServer.web.openapi.WebControllerManager;

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
  private final ServiceLocator myServiceLocator;
  @NotNull
  private final UserModel userModel;
  private final Pattern USER_EXTENDED_NAME = Pattern.compile("\\(([\\w.]+)\\)");

  public AjaxAvatarController(
          @NotNull WebControllerManager manager,
          @NotNull AvatarService avatarService,
          @NotNull ServiceLocator serviceLocator, @NotNull UserModel userModel) {
    this.avatarService = avatarService;
    myServiceLocator = serviceLocator;
    this.userModel = userModel;
    manager.registerController("/avatarAjax.html", this);
  }

  @Nullable
  @Override
  protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
    new AjaxRequestProcessor().processRequest(request, response, new AjaxRequestProcessor.RequestHandler() {
      public void handleRequest(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response, @NotNull final Element xmlResponse) {
        try {
          String avatarUrl;
          SUser user = null;
          String username = request.getParameter("username");
          String buildId = request.getParameter("buildId");
          String queuedId = request.getParameter("queuedId");

          if (null != username && !username.isEmpty()) {
            user = getUserByUsername(username);
          } else if (null != buildId && !buildId.isEmpty()) {
            user = getTriggeredByUser(buildId);
          } else if (null != queuedId && !queuedId.isEmpty()) {
            user = getQueuedTriggeredByUser(queuedId);
          }

          avatarUrl = (null == user) ? avatarService.getAvatarUrl(username) : avatarService.getAvatarUrl(user);
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
  private SUser getUserByUsername(@NotNull String username) throws InvalidUsernameException {
    SUser user = userModel.findUserAccount(null, username);
    if (null == user) {
      Matcher matcher = USER_EXTENDED_NAME.matcher(username);
      if (matcher.find()) {
        user = userModel.findUserAccount(null, matcher.group(1));
      }
    }

    return user;
  }

  @Nullable
  private SUser getTriggeredByUser(String buildIdParam) {
    int buildIdVal;
    try {
      buildIdVal = Integer.parseInt(buildIdParam);
    } catch (NumberFormatException e) {
      LOG.error(String.format("Failed to parse integer from %s", buildIdParam));
      return null;
    }

    SBuild build = myServiceLocator.getSingletonService(BuildsManager.class).findBuildInstanceById(buildIdVal);
    if (null != build) {
      return build.getTriggeredBy().getUser();
    }

    return null;
  }

  @Nullable
  private SUser getQueuedTriggeredByUser(String queueIdParam) {
    int queueId;
    try {
      queueId = Integer.parseInt(queueIdParam);
    } catch (NumberFormatException e) {
      LOG.error(String.format("Failed to parse integer from %s", queueIdParam));
      return null;
    }

    BuildPromotionManager buildPromotionManager = myServiceLocator.getSingletonService(BuildPromotionManager.class);
    BuildPromotion buildPromotion = buildPromotionManager.findPromotionById(queueId);
    if (null != buildPromotion) {
      SQueuedBuild queuedBuild = buildPromotion.getQueuedBuild();
      if (null != queuedBuild) {
        return queuedBuild.getTriggeredBy().getUser();
      }
    }
    return null;
  }
}
