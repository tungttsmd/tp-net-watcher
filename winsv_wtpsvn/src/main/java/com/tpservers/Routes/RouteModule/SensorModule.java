package com.tpservers.Routes.RouteModule;

import com.tpservers.Routes.RouteAnnotation.RouteCommandTitleAnnotation;
import com.tpservers.Routes.RouteAnnotation.RouteCommandAnnotation;
import com.tpservers.Routes.RouteDispatcher.RouteDispatcher;
import com.tpservers.Routes.RouteDispatcher.CommandDispatcher;

import com.google.gson.JsonObject;

@RouteCommandTitleAnnotation(routeCommandTitle = "sensor")
public final class SensorModule {

    @RouteCommandAnnotation(routeCommand = "raw")
    public static void sensorRaw(JsonObject root) {
        CommandDispatcher.send(root.get("route_host").getAsString(), "sensor", "raw", "");
    }

    @RouteCommandAnnotation(routeCommand = "temperatures")
    public static void sensorTemperature(JsonObject root) {
        CommandDispatcher.send(root.get("route_host").getAsString(), "sensor", "temperatures", "");
    }

    @RouteCommandAnnotation(routeCommand = "heartbeat")
    public static void sensorHeartbeat(JsonObject root) {
        CommandDispatcher.send(root.get("route_host").getAsString(), "sensor", "heartbeat", "");
    }
}
