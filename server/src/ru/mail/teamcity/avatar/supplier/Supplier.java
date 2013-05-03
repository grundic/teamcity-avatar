package ru.mail.teamcity.avatar.supplier;

import ru.mail.teamcity.avatar.supplier.impl.DirectUrlAvatarSupplier;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 02.05.13 19:27
 */

public enum Supplier {

  DIRECT_URL("Direct Url") {
    public AvatarSupplier get() {
      return new DirectUrlAvatarSupplier();
    }
  };

  private static final Map<String, Supplier> stringMap = new HashMap<String, Supplier>();

  static {
    for (Supplier supplier : values())
      stringMap.put(supplier.toString(), supplier);
  }

  private Supplier(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  public static Supplier fromString(String name) {
    return stringMap.get(name);
  }

  public abstract AvatarSupplier get();

  private String name;

  public String getName() {
    return name;
  }
}
