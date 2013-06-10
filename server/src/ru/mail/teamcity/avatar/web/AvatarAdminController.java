package ru.mail.teamcity.avatar.web;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.controllers.ActionErrors;
import jetbrains.buildServer.controllers.BaseFormXmlController;
import jetbrains.buildServer.controllers.admin.AdminPage;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.ServerPaths;
import jetbrains.buildServer.util.ExceptionUtil;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimpleCustomTab;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;
import ru.mail.teamcity.avatar.config.ConfigHelper;
import ru.mail.teamcity.avatar.config.Supplier;
import ru.mail.teamcity.avatar.config.Suppliers;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 16.05.13 23:59
 */
public class AvatarAdminController extends BaseFormXmlController {

  private final static Logger LOG = Logger.getInstance(AvatarAdminController.class.getName());

  private static final String TAB_ID = "avatar";

  @NotNull
  private final String myJspPagePath;

  @NotNull
  private final ServerPaths myServerPaths;

  @NotNull
  private final Map<String, AvatarSupplier> suppliers;

  public AvatarAdminController(
          @NotNull SBuildServer server,
          @NotNull final PluginDescriptor pluginDescriptor,
          @NotNull final WebControllerManager webControllerManager,
          @NotNull final PagePlaces pagePlaces,
          @NotNull ServerPaths serverPaths,
          @NotNull Map<String, AvatarSupplier> suppliers) {
    super(server);

    myJspPagePath = pluginDescriptor.getPluginResourcesPath("jsp/avatarAdminConfiguration.jsp");
    myServerPaths = serverPaths;
    this.suppliers = suppliers;

    webControllerManager.registerController("/admin/avatarAdminConfiguration.html", this);
    registerAdminTab(pluginDescriptor, pagePlaces);
  }

  @Override
  protected ModelAndView doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
    final ModelAndView modelAndView = new ModelAndView(myJspPagePath);
    final ActionErrors actionErrors = new ActionErrors();

    Suppliers suppliersBean = ConfigHelper.load(myServerPaths.getConfigDir(), suppliers);

    modelAndView.getModel().put("suppliers", suppliers);
    modelAndView.getModel().put("suppliersBean", suppliersBean);
    modelAndView.getModel().put("actionErrors", actionErrors);
    return modelAndView;
  }

  @Override
  protected void doPost(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Element xmlResponse) {

    ActionErrors actionErrors = new ActionErrors();

    String xml = request.getParameter("suppliersXml");
    Suppliers suppliersBean;
    try {
      suppliersBean = ConfigHelper.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
    } catch (Exception e) {
      LOG.error(ExceptionUtil.getDisplayMessage(e.getCause()));
      actionErrors.addError(ExceptionUtil.getDisplayMessage(e.getCause()), null);
      writeErrors(xmlResponse, actionErrors);
      return;
    }

    for (Supplier supplier : suppliersBean.getSupplierList()) {
      if (!suppliers.containsKey(supplier.getId())) {
        LOG.error(String.format("Unknown supplier id '%s' received!", supplier.getId()));
        actionErrors.addError(supplier.getId(), String.format("Unknown supplier %s", supplier.getId()));
      }
    }

    if (actionErrors.hasErrors()) {
      writeErrors(xmlResponse, actionErrors);
      return;
    }

    try {
      ConfigHelper.write(suppliersBean, myServerPaths.getConfigDir());
    } catch (JAXBException e) {
      LOG.error("Failed to serialize Supplier XML!");
      actionErrors.addError(null, "Failed to serialize Supplier XML! " + e.getCause().getMessage());
    }

    if (actionErrors.hasErrors()) {
      writeErrors(xmlResponse, actionErrors);
      return;
    }

    xmlResponse.addContent("Settings are successfully saved!");
  }

  private void registerAdminTab(@NotNull PluginDescriptor pluginDescriptor, @NotNull PagePlaces pagePlaces) {
    final SimpleCustomTab tab = new AdminPage(
            pagePlaces,
            TAB_ID,
            pluginDescriptor.getPluginResourcesPath("jsp/avatarAdminConfigurationTab.jsp"),
            "Avatar") {

      @NotNull
      public String getGroup() {
        return INTEGRATIONS_GROUP;
      }
    };

    tab.addJsFile(pluginDescriptor.getPluginResourcesPath("js/jquery-ui-1.10.3.sortable.min.js"));
    tab.addJsFile(pluginDescriptor.getPluginResourcesPath("js/xml2json.js"));
    tab.addJsFile(pluginDescriptor.getPluginResourcesPath("js/avatar-admin.js"));
    tab.register();
  }
}