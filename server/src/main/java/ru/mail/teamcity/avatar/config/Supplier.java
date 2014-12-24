package ru.mail.teamcity.avatar.config;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Grigory Chernyshev
 * Date: 18.05.13 15:18
 */
@SuppressWarnings("UnusedDeclaration")
@XmlRootElement
public class Supplier {
  String id;
  boolean enabled;

  Supplier() {
    // empty constructor for JAXB
  }

  public Supplier(String id, boolean enabled) {
    this.id = id;
    this.enabled = enabled;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}