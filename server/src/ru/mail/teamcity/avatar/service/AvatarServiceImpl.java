package ru.mail.teamcity.avatar.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.serverSide.ServerPaths;
import jetbrains.buildServer.users.PropertyKey;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.SimplePropertyKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mail.teamcity.avatar.config.ConfigHelper;
import ru.mail.teamcity.avatar.config.Supplier;
import ru.mail.teamcity.avatar.config.Suppliers;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * User: Grigory Chernyshev
 * Date: 05.05.13 22:51
 */
public class AvatarServiceImpl implements AvatarService {

  private final static Logger LOG = Logger.getInstance(AvatarServiceImpl.class.getName());

  private final Map<String, AvatarSupplier> suppliers;
  private final LoadingCache<SUser, String> cache;
  @NotNull
  private final ServerPaths serverPaths;

  private final String PROPERTY_KEY_NAME = "avatar.selected.supplier.type";
  private final PropertyKey PROPERTY_KEY = new SimplePropertyKey(PROPERTY_KEY_NAME);

  public AvatarServiceImpl(
          @NotNull Map<String, AvatarSupplier> suppliers,
          @NotNull CacheBuilder cacheBuilder,
          @NotNull ServerPaths serverPaths) {
    this.suppliers = suppliers;
    this.serverPaths = serverPaths;

    //noinspection unchecked
    this.cache = cacheBuilder.build(
            new CacheLoader<SUser, String>() {
              public String load(SUser user) {
                AvatarSupplier avatarSupplier = getAvatarSupplier(user);
                if (null == avatarSupplier) {
                  return "";
                }
                return avatarSupplier.getAvatarUrl(user);
              }
            });
  }

  @Nullable
  public AvatarSupplier getAvatarSupplier(@NotNull SUser user) {
    String avatarSupplierKey = user.getPropertyValue(PROPERTY_KEY);
    if (null == avatarSupplierKey) {
      return null;
    }

    return getAvatarSupplier(avatarSupplierKey);
  }

  @Nullable
  public AvatarSupplier getAvatarSupplier(@NotNull String avatarSupplierKey) {
    return suppliers.get(avatarSupplierKey);
  }

  @NotNull
  public Map<String, AvatarSupplier> getAllSuppliers() {
    return suppliers;
  }

  @NotNull
  public Map<String, AvatarSupplier> getEnabledSuppliers() {

    Map<String, AvatarSupplier> enabledSuppliers = new LinkedHashMap<String, AvatarSupplier>();
    Suppliers suppliersBean = ConfigHelper.load(serverPaths.getConfigDir(), suppliers);
    for (Supplier supplier : suppliersBean.getSupplierList()) {
      if (supplier.isEnabled()) {
        enabledSuppliers.put(supplier.getId(), suppliers.get(supplier.getId()));
      }
    }
    return enabledSuppliers;
  }


  public void store(@NotNull SUser user, @NotNull AvatarSupplier avatarSupplier, @NotNull Map<String, String[]> params) {
    user.setUserProperty(PROPERTY_KEY, avatarSupplier.getBeanName());
    avatarSupplier.store(user, params);
    cache.put(user, avatarSupplier.getAvatarUrl(user));
  }

  @NotNull
  public String getAvatarUrl(@NotNull SUser user) {
    try {
      return cache.get(user);
    } catch (ExecutionException e) {
      LOG.error(e.getCause());
      return "";
    }
  }
}
