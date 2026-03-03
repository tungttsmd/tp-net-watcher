package com.tpservers.Routes.RouteModule;

import com.tpservers.Routes.RouteAnnotation.RouteCommandTitleAnnotation;
import com.tpservers.Routes.RouteAnnotation.RouteCommandAnnotation;
import com.tpservers.Routes.RouteDispatcher.RouteDispatcher;
import com.tpservers.Routes.RouteDispatcher.CommandDispatcher;
import com.tpservers.Services.Facade.WakeOnLanService;

import com.google.gson.JsonObject;

@RouteCommandTitleAnnotation(routeCommandTitle = "power")
public final class PowerModule {

    @RouteCommandAnnotation(routeCommand = "power-on")
    public static void powerOn(JsonObject root, String hostId) {

        String nodeLocalIp = WakeOnLanService.wakeOnLanePowerOnByHostId(hostId);

        CommandDispatcher.send(root.get("route_host").getAsString(), "power", "power-on", "wake-on-lan-" + nodeLocalIp);
    }

    @RouteCommandAnnotation(routeCommand = "power-off")
    public static void powerOff(JsonObject root) {
        CommandDispatcher.send(root.get("route_host").getAsString(), "power", "power-off", "");
    }

    @RouteCommandAnnotation(routeCommand = "power-reset")
    public static void powerReset(JsonObject root) {
        CommandDispatcher.send(root.get("route_host").getAsString(), "power", "power-reset", "");
    }
}
