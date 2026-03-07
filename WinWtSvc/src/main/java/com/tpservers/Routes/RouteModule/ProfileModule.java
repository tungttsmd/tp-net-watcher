package com.tpservers.Routes.RouteModule;

import com.tpservers.Routes.RouteAnnotation.RouteCommandTitleAnnotation;
import com.tpservers.Routes.RouteAnnotation.RouteCommandAnnotation;
import com.tpservers.Routes.RouteDispatcher.RouteDispatcher;
import com.tpservers.Routes.RouteDispatcher.CommandDispatcher;

import com.google.gson.JsonObject;

@RouteCommandTitleAnnotation(routeCommandTitle = "profile")
public final class ProfileModule {

    @RouteCommandAnnotation(routeCommand = "hardware-profile")
    public static void hardwareProfile(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "profile", "hardware-profile", "");
    }

    @RouteCommandAnnotation(routeCommand = "hardware-network")
    public static void hardwareNetwork(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "profile", "hardware-network", "");
    }

    @RouteCommandAnnotation(routeCommand = "hardware-device")
    public static void hardwareDevice(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "profile", "hardware-device", "");
    }

    @RouteCommandAnnotation(routeCommand = "hardware-cpu")
    public static void hardwareCpu(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "profile", "hardware-cpu", "");
    }

    @RouteCommandAnnotation(routeCommand = "hardware-gpu")
    public static void hardwareGpu(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "profile", "hardware-gpu", "");
    }

    @RouteCommandAnnotation(routeCommand = "hardware-ram")
    public static void hardwareRam(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "profile", "hardware-ram", "");
    }

    @RouteCommandAnnotation(routeCommand = "hardware-disk")
    public static void hardwareDisk(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "profile", "hardware-disk", "");
    }

    @RouteCommandAnnotation(routeCommand = "hardware-motherboard")
    public static void hardwareMotherboard(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "profile", "hardware-motherboard", "");
    }

    @RouteCommandAnnotation(routeCommand = "hardware-os")
    public static void hardwareOs(JsonObject root) {
        CommandDispatcher.send(root.get("route_host_identifier")
                .getAsString(), "profile", "hardware-os", "");
    }
}
