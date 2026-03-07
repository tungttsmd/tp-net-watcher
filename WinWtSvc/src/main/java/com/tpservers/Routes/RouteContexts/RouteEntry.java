package com.tpservers.Routes.RouteContexts;

import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.Method;

public record RouteEntry(
        String routeCommandTitle,
        String routeCommand,
        Method routeHandle
    ) {
    }