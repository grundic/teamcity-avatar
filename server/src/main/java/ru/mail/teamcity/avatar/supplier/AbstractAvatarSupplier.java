package ru.mail.teamcity.avatar.supplier;

import jetbrains.buildServer.util.PropertiesUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanNameAware;
import ru.mail.teamcity.avatar.AppConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * User: Grigory Chernyshev
 * Date: 07.05.13 19:33
 */
public abstract class AbstractAvatarSupplier implements AvatarSupplier, BeanNameAware {
  private String BEAN_NAME;

  public void setBeanName(String s) {
    this.BEAN_NAME = s;
  }

  @NotNull
  public String getAvatarUrl(String identifier) {
    return "";
  }

  @NotNull
  public String getBeanName() {
    return this.BEAN_NAME;
  }

  protected Properties readProperties(String serverConfigDir) throws IOException {
    return PropertiesUtil.loadProperties(new File(serverConfigDir, AppConfiguration.PLUGIN_PROPERTIES)
    );
  }
}
