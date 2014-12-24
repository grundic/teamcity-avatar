package ru.mail.teamcity.avatar.extensions.pageExtension;

import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimplePageExtension;
import org.jetbrains.annotations.NotNull;
import ru.mail.teamcity.avatar.AppConfiguration;

import java.util.Collections;

/**
 * User: Grigory Chernyshev
 * Date: 27.04.13 23:20
 */
public class JsInjectionExtension extends SimplePageExtension {

    public JsInjectionExtension(
            @NotNull SBuildServer server,
            @NotNull PagePlaces pagePlaces,
            @NotNull PluginDescriptor descriptor) {
        super(pagePlaces, PlaceId.ALL_PAGES_FOOTER, "teamcity-avatar", descriptor.getPluginResourcesPath("jsp/avatar.jsp"));
        addCssFile(descriptor.getPluginResourcesPath("css/avatar.css"));

        int version = (int) server.getServerMajorVersion();
        int minVersion = Collections.min(AppConfiguration.SUPPORTED_VERSIONS);
        if (version >= minVersion) {
            int suitableVersion = AppConfiguration.SUPPORTED_VERSIONS.contains(version) ?
                    version : Collections.max(AppConfiguration.SUPPORTED_VERSIONS);

            addJsFile(descriptor.getPluginResourcesPath("js/avatar-common.js"));
            addJsFile(descriptor.getPluginResourcesPath(String.format("js/avatar-%d.js", suitableVersion)));
            register();
        }
    }
}
