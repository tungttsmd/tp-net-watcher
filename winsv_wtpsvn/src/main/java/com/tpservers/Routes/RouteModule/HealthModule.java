package com.tpservers.Routes.RouteModule;

import com.tpservers.Routes.RouteAnnotation.RouteCommandTitleAnnotation;
import com.tpservers.Routes.RouteAnnotation.RouteCommandAnnotation;
import com.tpservers.Routes.RouteDispatcher.RouteDispatcher;
import com.tpservers.Routes.RouteDispatcher.CommandDispatcher;

import com.google.gson.JsonObject;

@RouteCommandTitleAnnotation(routeCommandTitle = "health")
public final class HealthModule {

    @RouteCommandAnnotation(routeCommand = "heartbeat")
    public static void healthHeartbeat(JsonObject root) {
        CommandDispatcher.send(root.get("route_host").getAsString(), "health", "heartbeat", "");
    }
}
