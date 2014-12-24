package ru.mail.teamcity.avatar.config;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Grigory Chernyshev
 * Date: 18.05.13 10:46
 */

@SuppressWarnings("UnusedDeclaration")
@XmlRootElement
public class Suppliers {
  @NotNull
  private List<Supplier> supplierList = new ArrayList<Supplier>();

  private boolean individualEnabled;

  @NotNull
  @XmlElement(name = "supplier")
  public List<Supplier> getSupplierList() {
    return supplierList;
  }

  public void setSupplierList(@NotNull List<Supplier> supplierList) {
    this.supplierList = supplierList;
  }

  public void addSupplier(Supplier supplier) {
    this.supplierList.add(supplier);
  }

  public boolean isIndividualEnabled() {
    return individualEnabled;
  }

  public void setIndividualEnabled(boolean individualEnabled) {
    this.individualEnabled = individualEnabled;
  }
}