package com.tpservers.Routes.RouteModule;

import com.tpservers.Routes.RouteAnnotation.RouteCommandTitleAnnotation;
import com.tpservers.Routes.RouteAnnotation.RouteCommandAnnotation;
import com.tpservers.Routes.RouteDispatcher.RouteDispatcher;
import com.tpservers.Routes.RouteDispatcher.CommandDispatcher;

import com.google.gson.JsonObject;

@RouteCommandTitleAnnotation(routeCommandTitle = "update")
public final class UpdateModule {

    @RouteCommandAnnotation(routeCommand = "current-version")
    public static void updateCurrentVersion(JsonObject root) {
        CommandDispatcher.send(root.get("route_host").getAsString(), "update", "current-version", "");
    }

    @RouteCommandAnnotation(routeCommand = "lastest-version")
    public static void updateLatestVersion(JsonObject root) {
        CommandDispatcher.send(root.get("route_host").getAsString(), "update", "lastest-version", "");
    }

    @RouteCommandAnnotation(routeCommand = "execute")
    public static void updateExecute(JsonObject root) {
        CommandDispatcher.send(root.get("route_host").getAsString(), "update", "execute", "");
    }
}
