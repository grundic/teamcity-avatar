package ru.mail.teamcity.avatar.supplier.impl;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.User;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.serverSide.ServerPaths;
import jetbrains.buildServer.users.SUser;
import org.jetbrains.annotations.NotNull;
import ru.mail.teamcity.avatar.supplier.AbstractAvatarSupplier;
import ru.mail.teamcity.avatar.supplier.IndividualAvatarSupplier;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User: Grigory Chernyshev
 * Date: 24.05.13 18:41
 */
public class JiraAvatarSupplier extends AbstractAvatarSupplier implements IndividualAvatarSupplier {
  private final static Logger LOG = Logger.getInstance(JiraAvatarSupplier.class.getName());
  private static final String JIRA_SERVER_URL = "jira.server.url";
  private static final String JIRA_USERNAME = "jira.username";
  private static final String JIRA_PASSWORD = "jira.password";

  @NotNull
  private final ServerPaths serverPaths;

  public JiraAvatarSupplier(@NotNull ServerPaths serverPaths) {
    this.serverPaths = serverPaths;
  }

  @NotNull
  public String getAvatarUrl(SUser user) {
    JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
    URI jiraServerUri;
    try {
      Properties properties = readProperties(serverPaths.getConfigDir());
      String url = properties.getProperty(JIRA_SERVER_URL);
      String username = properties.getProperty(JIRA_USERNAME);
      String password = properties.getProperty(JIRA_PASSWORD);
      if (null == url || null == username || null == password) {
        LOG.warn("Jira supplier is not configured!");
        return "";
      }

      jiraServerUri = new URI(url);
      JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, username, password);

      User jiraUser = restClient.getUserClient().getUser(user.getUsername(), new NullProgressMonitor());
      return jiraUser.getAvatarUri().toString();
    } catch (Exception e) {
      // TODO: pass error to velocity
      LOG.error(e.getMessage());
    }
    return "";
  }

  @NotNull
  public String getOptionName() {
    return "Jira";
  }

  @NotNull
  public String getTemplate() {
    return "ru/mail/teamcity/avatar/templates/jira.vm";
  }

  @NotNull
  public Map<String, Object> getTemplateParams(@NotNull SUser user) {
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("avatarUrl", getAvatarUrl(user));
    return params;
  }

  public void store(SUser user, Map<String, String[]> params) {
  }
}
