package com.tpservers.Routes.RouteModule;

import com.tpservers.Routes.RouteAnnotation.RouteCommandTitleAnnotation;
import com.tpservers.Routes.RouteAnnotation.RouteCommandAnnotation;
import com.tpservers.Routes.RouteDispatcher.RouteDispatcher;
import com.tpservers.Routes.RouteDispatcher.CommandDispatcher;

import com.google.gson.JsonObject;

@RouteCommandTitleAnnotation(routeCommandTitle = "launch")
public final class LaunchModule {

    @RouteCommandAnnotation(routeCommand = "launch-device-type")
    public static void launchDeviceType(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "launch", "launch-device-type", "");
    }

    @RouteCommandAnnotation(routeCommand = "launch-current-version")
    public static void launchCurrentVersion(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "launch", "launch-current-version", "");
    }

    @RouteCommandAnnotation(routeCommand = "launch-update")
    public static void launchUpdate(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "launch", "launch-update", "");
    }

    @RouteCommandAnnotation(routeCommand = "launch-set-wallpaper")
    public static void launchSetWallpaper(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "launch", "launch-set-wallpaper", "");
    }
}
