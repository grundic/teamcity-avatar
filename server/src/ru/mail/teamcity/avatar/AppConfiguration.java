package ru.mail.teamcity.avatar;

import java.util.Arrays;
import java.util.List;

/**
 * User: Grigory Chernyshev
 * Date: 28.04.13 23:48
 */
public class AppConfiguration {
  public final static String PLUGIN_NAME = "teamcity-avatar";
  public final static String PLUGIN_PROPERTIES = PLUGIN_NAME + ".properties";

  public final static List<Integer> SUPPORTED_VERSIONS = Arrays.asList(7, 8);
}
