package ru.mail.teamcity.avatar.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * User: Grigory Chernyshev
 * Date: 18.05.13 10:46
 */

@SuppressWarnings("UnusedDeclaration")
@XmlRootElement
public class Suppliers {
  private List<Supplier> supplierList;

  @XmlElement(name = "supplier")
  public List<Supplier> getSupplierList() {
    return supplierList;
  }

  public void setSupplierList(List<Supplier> supplierList) {
    this.supplierList = supplierList;
  }

}

@SuppressWarnings("UnusedDeclaration")
@XmlRootElement
class Supplier {
  String id;
  boolean enabled;


  Supplier() {
    // empty constructor for JAXB
  }

  Supplier(String id, boolean enabled) {
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