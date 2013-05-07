package ru.mail.teamcity.avatar.supplier;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanNameAware;

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
  public String getBeanName() {
    return this.BEAN_NAME;
  }
}
