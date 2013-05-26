package ru.mail.teamcity.avatar.web;

import jetbrains.buildServer.controllers.ActionErrors;
import jetbrains.buildServer.controllers.AjaxRequestProcessor;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.util.SessionUser;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.jdom.CDATA;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.ModelAndView;
import ru.mail.teamcity.avatar.service.AvatarService;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 03.05.13 12:38
 */
public class AvatarProfileSupplierTemplateController extends BaseController {

  @NotNull
  private final AvatarService avatarService;
  @NotNull
  private VelocityEngine velocityEngine;

  public AvatarProfileSupplierTemplateController(
          @NotNull WebControllerManager manager,
          @NotNull PluginDescriptor pluginDescriptor,
          @NotNull SBuildServer server,
          @NotNull AvatarService avatarService
  ) {
    this.avatarService = avatarService;
    initVelocity(server, pluginDescriptor);

    manager.registerController("/avatarProfileSupplierTemplate.html", this);
  }

  private void initVelocity(@NotNull SBuildServer server, @NotNull PluginDescriptor pluginDescriptor) {
    velocityEngine = new VelocityEngine();
    velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, server.getServerRootPath() + pluginDescriptor.getPluginResourcesPath());
    velocityEngine.init();
  }

  @Nullable
  @Override
  protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
    new AjaxRequestProcessor().processRequest(request, response, new AjaxRequestProcessor.RequestHandler() {
      public void handleRequest(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Element xmlResponse) {
        final ActionErrors actionErrors = new ActionErrors();
        SUser user = SessionUser.getUser(request);

        String avatarSupplierKey = request.getParameter("avatarSupplierType");
        if (null == avatarSupplierKey) {
          actionErrors.addError("emptySupplierKey", "Avatar supplier key is not found in request!");
          actionErrors.serialize(xmlResponse);
          return;
        }

        AvatarSupplier avatarSupplier = avatarService.getAvatarSupplier(avatarSupplierKey);
        if (null == avatarSupplier) {
          actionErrors.addError("wrongSupplier", String.format("Avatar supplier '%s' does not exists!", avatarSupplierKey));
          actionErrors.serialize(xmlResponse);
          return;
        }

        String html = renderTemplate(avatarSupplier.getTemplate(), avatarSupplier.getTemplateParams(user));

        final Element configElement = new Element("html");
        configElement.setContent(new CDATA(html));
        xmlResponse.addContent(configElement);
      }
    });
    return null;
  }

  @NotNull
  private String renderTemplate(@NotNull String path, Map<String, Object> parameters) {
    VelocityContext context = new VelocityContext(parameters);
    Template template = velocityEngine.getTemplate(path);
    StringWriter writer = new StringWriter();

    template.merge(context, writer);
    return writer.toString();
  }
}
