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