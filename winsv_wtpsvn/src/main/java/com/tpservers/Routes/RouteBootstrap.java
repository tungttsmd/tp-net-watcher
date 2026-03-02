package com.tpservers.Routes;

import com.tpservers.Routes.RouteRegistry.RouteRegistry;
import com.tpservers.Routes.RouteAnnotation.RouteCommandTitleAnnotation;

import org.reflections.Reflections;


public final class RouteBootstrap {

    public static void init() {

        Reflections ref = new Reflections(
            "com.tpservers.Routes.RouteModule"
        );

        for (Class<?> cls : ref.getTypesAnnotatedWith(RouteCommandTitleAnnotation.class)) {
            try {
                RouteRegistry.register(cls);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
