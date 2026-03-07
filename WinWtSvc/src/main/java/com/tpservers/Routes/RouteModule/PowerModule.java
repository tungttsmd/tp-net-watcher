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
    public static void powerOn(JsonObject root) {

        // Xử lý chuỗi host identifier: example tp-net-agent-85-12F20543
        String[] parts = root.get("hostIdentifier").getAsString().split("-");
        String hostId = parts[parts.length - 2];

        String nodeLocalIp = WakeOnLanService.wakeOnLanePowerOnByHostId(hostId);

        String context = "wake-on-lan-" + nodeLocalIp;

        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "power", "power-on", context);
    }

    @RouteCommandAnnotation(routeCommand = "power-off")
    public static void powerOff(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "power", "power-off", "");
    }

    @RouteCommandAnnotation(routeCommand = "power-reset")
    public static void powerReset(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "power", "power-reset", "");
    }
}
