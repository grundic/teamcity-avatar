package ru.mail.teamcity.avatar.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * User: Grigory Chernyshev
 * Date: 12.05.13 23:31
 */
public class AvatarCache {
  private static ConcurrentMap<String, String> cache = new ConcurrentHashMap<String, String>();

  @NotNull
  public static String getCache(@NotNull String username) {
    return cache.get(username);
  }

  public static void setCache(@NotNull String username, @Nullable String url) {
    AvatarCache.cache.put(username, url);
  }
}
