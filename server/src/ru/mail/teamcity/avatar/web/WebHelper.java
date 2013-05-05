package ru.mail.teamcity.avatar.web;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.Nullable;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;
import ru.mail.teamcity.avatar.supplier.Supplier;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Grigory Chernyshev
 * Date: 05.05.13 18:32
 */
public class WebHelper {

  private final static Logger LOG = Logger.getInstance(AvatarSupplierAjaxController.class.getName());

  public final static String SUPPLIER_PARAMETER = "avatarSupplierType";

  @Nullable
  public static AvatarSupplier getAvatarSupplier(HttpServletRequest request) {
    String supplierName = request.getParameter(SUPPLIER_PARAMETER);
    if (null == supplierName) {
      LOG.warn("Supplier parameter is empty!");
      return null;
    }

    Supplier supplier = Supplier.fromString(supplierName);
    if (null == supplier) {
      LOG.warn(String.format("Failed to get supplier by name '%s'", supplierName));
      return null;
    }

    AvatarSupplier avatarSupplier = supplier.get();
    if (null == avatarSupplier) {
      LOG.error(String.format("Failed to get instance of supplier %s!", supplier));
      return null;
    }
    return avatarSupplier;
  }
}
