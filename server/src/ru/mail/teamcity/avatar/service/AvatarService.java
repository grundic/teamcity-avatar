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
   * Return registered avatar suppliers
   */
  @NotNull
  public Map<String, AvatarSupplier> getSuppliers();

  /*
   * Save avatar supplier, selected by user and supplier's settings
   */
  public void store(@NotNull SUser user, @NotNull AvatarSupplier avatarSupplier, @NotNull Map<String, String[]> params);

  /*
   * Get avatar url for user
   */
  @NotNull
  public String getAvatarUrl(@NotNull SUser user);
}
