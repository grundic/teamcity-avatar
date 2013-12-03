package ru.mail.teamcity.avatar.supplier.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jetbrains.buildServer.users.SUser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.jetbrains.annotations.NotNull;
import ru.mail.teamcity.avatar.supplier.AbstractAvatarSupplier;
import ru.mail.teamcity.avatar.supplier.IndividualAvatarSupplier;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 21.06.13 0:15
 */
public class TumblrAvatarSupplier extends AbstractAvatarSupplier implements IndividualAvatarSupplier {
  @NotNull
  public String getAvatarUrl(SUser user) {
    return doGetAvatarUrl(user.getUsername());
  }

  @NotNull
  @Override
  public String getAvatarUrl(String identifier) {
    return doGetAvatarUrl(identifier);
  }

  private String doGetAvatarUrl(String name) {
    try {
      HttpClient client = new DefaultHttpClient();
      HttpGet getRequest = new HttpGet(String.format("http://api.tumblr.com/v2/blog/%s/avatar", name));
      getRequest.addHeader("accept", "application/json");

      HttpParams params = new BasicHttpParams();
      params.setParameter("http.protocol.handle-redirects", false);
      getRequest.setParams(params);

      HttpResponse response = client.execute(getRequest);

      ObjectMapper objectMapper = new ObjectMapper();
      if (response.getStatusLine().getStatusCode() == 404) {
        return "";
      }
      TumblrAvatar tumblrAvatar = objectMapper.readValue(response.getEntity().getContent(), TumblrAvatar.class);

      if (tumblrAvatar.getMeta().getStatus().equalsIgnoreCase("301")) {
        return tumblrAvatar.getResponse().getAvatar_url();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  @NotNull
  public String getOptionName() {
    return "Tumblr";
  }

  @NotNull
  public String getTemplate() {
    return "templates/tumblr.vm";
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

/**
 * Bean for Tumblr avatar api response.
 */
@SuppressWarnings("UnusedDeclaration")
class TumblrAvatar {
  private Meta _meta;
  private Response _response;

  Meta getMeta() {
    return _meta;
  }

  void setMeta(Meta meta) {
    _meta = meta;
  }

  Response getResponse() {
    return _response;
  }

  void setResponse(Response response) {
    _response = response;
  }

  public static class Meta {
    private String _status, _msg;

    public String getStatus() {
      return _status;
    }

    public void setStatus(String status) {
      _status = status;
    }

    public String getMsg() {
      return _msg;
    }

    public void setMsg(String msg) {
      _msg = msg;
    }
  }

  public static class Response {
    private String _avatar_url;

    public String getAvatar_url() {
      return _avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
      _avatar_url = avatar_url;
    }
  }
}