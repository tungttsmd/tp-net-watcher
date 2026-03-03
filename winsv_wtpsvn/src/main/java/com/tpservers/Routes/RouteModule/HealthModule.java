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

    @RouteCommandAnnotation(routeCommand = "health-network")
    public static void healthNetwork(JsonObject root) {
        JsonObject args = new JsonObject();
        if (root.has("payload_read")) {
            JsonObject payload = root.getAsJsonObject("payload_read");
            if (payload.has("ping_count")) args.add("ping_count", payload.get("ping_count"));
            if (payload.has("targets"))    args.add("targets",    payload.get("targets"));
        }
        CommandDispatcher.send(
            root.get("route_host").getAsString(),
            "health",
            "health-network",
            args.toString());
    }
}
