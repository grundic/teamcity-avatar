package ru.mail.teamcity.avatar.web;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Grigory Chernyshev
 * Date: 05.05.13 18:32
 */
public class WebHelper {

  private final static Logger LOG = Logger.getInstance(AvatarSupplierAjaxController.class.getName());

  public final static String SUPPLIER_PARAMETER = "avatarSupplierType";

  @Nullable
  public static String getAvatarSupplierKey(@NotNull HttpServletRequest request) {
    String avatarSupplierKey = request.getParameter(SUPPLIER_PARAMETER);
    if (null == avatarSupplierKey) {
      LOG.warn("Can't extract avatar supplier key from request!");
      return null;
    }

    return avatarSupplierKey;
  }
}
