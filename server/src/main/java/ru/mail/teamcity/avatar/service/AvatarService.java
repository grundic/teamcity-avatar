package ru.mail.teamcity.avatar.service;

import jetbrains.buildServer.users.SUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;

import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 05.05.13 22:46
 */
public interface AvatarService {

  /*
   * Return configured avatar supplier for user
   */
  @Nullable
  public AvatarSupplier getAvatarSupplier(@NotNull SUser user);

  /*
   * Return configured avatar supplier by key
   */
  @Nullable
  public AvatarSupplier getAvatarSupplier(@NotNull String avatarSupplierKey);

  /*
   * Return all available avatar suppliers
   */
  @NotNull
  public Map<String, AvatarSupplier> getAllSuppliers();

  /*
   * Return all enabled avatar suppliers.
   * Administrator can enable/disable supplier in admin area.
   */
  @NotNull
  public Map<String, AvatarSupplier> getEnabledSuppliers();


  /*
   * Save avatar supplier, selected by user and supplier's settings.
   */
  public void store(@NotNull SUser user, @Nullable AvatarSupplier avatarSupplier, @NotNull Map<String, String[]> params);

  /*
   * Get avatar url for Teamcity user.
   */
  @NotNull
  public String getAvatarUrl(@NotNull SUser user);

  /*
   * Get avatar url by string identifier.
   */
  @NotNull
  public String getAvatarUrl(@Nullable String identifier);

  public void flushCache();
}
