package ru.mail.teamcity.avatar.supplier.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.serverSide.ServerPaths;
import jetbrains.buildServer.users.SUser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.jetbrains.annotations.NotNull;
import ru.mail.teamcity.avatar.AppConfiguration;
import ru.mail.teamcity.avatar.supplier.AbstractAvatarSupplier;
import ru.mail.teamcity.avatar.supplier.IndividualAvatarSupplier;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * User: g.chernyshev
 * Date: 24.06.13
 * Time: 17:53
 */
public class SysMailRuAvatarSupplier extends AbstractAvatarSupplier implements IndividualAvatarSupplier {

  private final static Logger LOG = Logger.getInstance(SysMailRuAvatarSupplier.class.getName());
  private static final String MAILRU_API_APP = "mail.ru.api.app";
  private static final String MAILRU_API_KEY = "mail.ru.api.key";
  @NotNull
  private final ServerPaths serverPaths;
  private List<String> errors;

  public SysMailRuAvatarSupplier(@NotNull ServerPaths serverPaths) {
    this.serverPaths = serverPaths;
  }

  @NotNull
  public String getAvatarUrl(SUser user) {
    try {
      Properties properties = readProperties(serverPaths.getConfigDir());
      String apiApp = properties.getProperty(MAILRU_API_APP);
      String apiKey = properties.getProperty(MAILRU_API_KEY);

      if (null == apiApp || null == apiKey) {
        errors.add(String.format("%s supplier is not configured: please add %s and %s configuration items in config.", getOptionName(), MAILRU_API_APP, MAILRU_API_KEY));
        return "";
      }

      HttpClient client = new DefaultHttpClient();
      HttpGet getRequest = new HttpGet(String.format(
              "https://sys.mail.ru/api/v1/user/?format=json&api_app=%s&api_key=%s&username=%s", apiApp, apiKey, user.getUsername()));
      getRequest.addHeader("accept", "application/json");

      HttpParams params = new BasicHttpParams();
      getRequest.setParams(params);

      HttpResponse response = client.execute(getRequest);
      if (response.getStatusLine().getStatusCode() == 401) {
        this.errors.add("Authorization failed! Please, check yor application and key values in config file.");
        return "";
      }

      ObjectMapper objectMapper = new ObjectMapper();
      SysMailRuResponse sysMailRuResponse = objectMapper.readValue(response.getEntity().getContent(), SysMailRuResponse.class);
      if (sysMailRuResponse.getObjects().size() == 1) {
        return String.format("http://sys.mail.ru%s", sysMailRuResponse.getObjects().get(0).getProfile().getFoto());
      } else {
        errors.add(String.format("Can't find suitable user in sys.mail.ru catalog for your username '%s'", user.getUsername()));
      }
    } catch (FileNotFoundException e) {
      this.errors.add(String.format("Failed to locate configuration file %s/%s", serverPaths.getConfigDir(), AppConfiguration.PLUGIN_PROPERTIES));
    } catch (Exception e) {
      e.printStackTrace();
      this.errors.add(e.getMessage());
    }
    return "";
  }

  @NotNull
  public String getOptionName() {
    return "Sys.Mail.ru";
  }

  @NotNull
  public String getTemplate() {
    return "templates/sys.mail.ru.vm";
  }

  @NotNull
  public Map<String, Object> getTemplateParams(@NotNull SUser user) {
    this.errors = new ArrayList<String>();
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("avatarUrl", getAvatarUrl(user));
    params.put("errors", errors);
    return params;
  }

  public void store(SUser user, Map<String, String[]> params) {
  }
}

@SuppressWarnings("UnusedDeclaration")
@JsonIgnoreProperties(ignoreUnknown = true)
class SysMailRuResponse {
  List<ObjectItem> objects;

  List<ObjectItem> getObjects() {
    return objects;
  }

  void setObjects(List<ObjectItem> objects) {
    this.objects = objects;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ObjectItem {
    private Profile profile;

    Profile getProfile() {
      return profile;
    }

    void setProfile(Profile profile) {
      this.profile = profile;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile {
      private String foto;

      String getFoto() {
        return foto;
      }

      void setFoto(String foto) {
        this.foto = foto;
      }
    }
  }
}