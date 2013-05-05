/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.mail.teamcity.avatar.service;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.users.PropertyKey;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.SimplePropertyKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;
import ru.mail.teamcity.avatar.supplier.Supplier;

import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 05.05.13 22:51
 */
public class AvatarServiceImpl implements AvatarService {

  private final static Logger LOG = Logger.getInstance(AvatarServiceImpl.class.getName());

  private final String PROPERTY_KEY_NAME = "avatar.selected.supplier.type";
  private final PropertyKey PROPERTY_KEY = new SimplePropertyKey(PROPERTY_KEY_NAME);

  @Nullable
  public AvatarSupplier getAvatarSupplier(@NotNull SUser user) {
    String value = user.getPropertyValue(PROPERTY_KEY);
    if (null == value){
      return null;
    }

    Supplier supplier = Supplier.fromString(value);
    if (null == supplier){
      LOG.error(String.format("Failed to locate supplier '%s' for user '%s'!", value, user.getName()));
      return null;
    }

    return supplier.get();
  }

  public void store(@NotNull SUser user, @NotNull String avatarSupplier, @NotNull Map<String, String[]> params) {
    Supplier supplier = Supplier.fromString(avatarSupplier);
    if (null == supplier){
      return;
    }
    store(user, avatarSupplier, params);
  }

  public void store(@NotNull SUser user, @NotNull AvatarSupplier avatarSupplier, @NotNull Map<String, String[]> params) {
    user.setUserProperty(PROPERTY_KEY, avatarSupplier.getClass().getName());
    avatarSupplier.store(user, params);
  }

  @Nullable
  public String getAvatarUrl(@NotNull SUser user) {
    AvatarSupplier avatarSupplier = getAvatarSupplier(user);
    if (null == avatarSupplier){
      return null;
    }
    return avatarSupplier.getAvatarUrl(user);
  }
}
