package ru.mail.teamcity.avatar.web;

import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 03.05.13 13:28
 */
public abstract class AbstractAjaxController extends BaseController {

  @NotNull
  protected final PluginDescriptor pluginDescriptor;
  @NotNull
  protected final SBuildServer server;
  protected VelocityEngine velocityEngine;

  protected AbstractAjaxController(@NotNull PluginDescriptor pluginDescriptor, @NotNull SBuildServer server) throws Exception {
    this.pluginDescriptor = pluginDescriptor;
    this.server = server;
    initVelocity();
  }

  private void initVelocity() throws Exception {
    velocityEngine = new VelocityEngine();
    velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, server.getServerRootPath() + pluginDescriptor.getPluginResourcesPath());

    // FIXME Turn caching on for deployment.
    velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, false);
    velocityEngine.init();
  }

  protected String renderTemplate(@NotNull String path, Map<String, Object> parameters) {
    VelocityContext context = new VelocityContext(parameters);
    Template template = velocityEngine.getTemplate(path);
    StringWriter writer = new StringWriter();

    template.merge(context, writer);
    return writer.toString();
  }

  protected void write(@NotNull HttpServletResponse response, @NotNull String message) throws IOException {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html");
    response.getWriter().write(message);
  }
}
