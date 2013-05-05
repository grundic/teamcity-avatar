package ru.mail.teamcity.avatar.supplier;

import org.jetbrains.annotations.Nullable;
import ru.mail.teamcity.avatar.supplier.impl.DirectUrlAvatarSupplier;
import ru.mail.teamcity.avatar.supplier.impl.DisabledAvatarSupplier;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 02.05.13 19:27
 */

public enum Supplier {

  DISABLED(DisabledAvatarSupplier.class),
  DIRECT_URL(DirectUrlAvatarSupplier.class);


  private static final Map<String, Supplier> stringMap = new HashMap<String, Supplier>();

  static {
    for (Supplier supplier : values())
      stringMap.put(supplier.toString(), supplier);
  }

  private Supplier(Class<? extends AvatarSupplier> clazz) {
    this.clazz = clazz;
  }

  @Override
  public String toString() {
    return clazz.getName();
  }

  public static Supplier fromString(String name) {
    return stringMap.get(name);
  }


  @Nullable
  public AvatarSupplier get() {
    try {
      return clazz.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
      return null;
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }
  }

  private Class<? extends AvatarSupplier> clazz;
}
