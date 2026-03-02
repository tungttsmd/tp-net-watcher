package com.tpservers.Routes.RouteRegistry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.tpservers.Routes.RouteAnnotation.RouteCommandTitleAnnotation;
import com.tpservers.Routes.RouteAnnotation.RouteCommandAnnotation;
import com.tpservers.Services.Facade.ConsoleService;

import com.tpservers.Routes.RouteContexts.RouteEntry;

public final class RouteRegistry {

    private static final Map<String, RouteEntry> ROUTE_REGISTRY_LIST = new HashMap<>();


    public static RouteEntry get(String routeCommandTitle, String routeCommand) {

        /* Key format for Route Registry List: routeCommandTitle:routeCommand */
        return ROUTE_REGISTRY_LIST.get(routeCommandTitle + ":" + routeCommand);
    }
    
    public static void register(Class<?> reflectClass) {

        try {

            RouteCommandTitleAnnotation routeModule = reflectClass.getAnnotation(RouteCommandTitleAnnotation.class);
            if (routeModule == null) return;

            for (Method routeHandle : reflectClass.getDeclaredMethods()) {

                RouteCommandAnnotation RouteCommandAnnotation = routeHandle.getAnnotation(RouteCommandAnnotation.class);
                if (RouteCommandAnnotation == null) continue;

                String registryListKey = routeModule.routeCommandTitle() + ":" + RouteCommandAnnotation.routeCommand();
                ROUTE_REGISTRY_LIST.put(registryListKey, new RouteEntry(routeModule.routeCommandTitle(), RouteCommandAnnotation.routeCommand(), routeHandle));
            }

        } catch (Exception e) {
            ConsoleService.error(e.getMessage());
        }
    }

    public static Map<String, RouteEntry> getRouteRegistryList() {
        return ROUTE_REGISTRY_LIST;
    }
}

