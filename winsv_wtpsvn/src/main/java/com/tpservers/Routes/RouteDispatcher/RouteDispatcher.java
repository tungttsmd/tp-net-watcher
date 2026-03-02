package com.tpservers.Routes.RouteDispatcher;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Spark.*;

import static spark.Spark.post;


import com.tpservers.Services.Facade.ResponseService;
import com.tpservers.Services.Facade.RedisService;
import com.tpservers.Services.Facade.ConsoleService;
import com.tpservers.Services.Facade.PoolService;

import tungtt.Pool.Facade.PoolFacade;
import tungtt.Console.Console;

import redis.clients.jedis.Jedis;

import com.tpservers.Routes.RouteRegistry.*;

import com.tpservers.Routes.RouteContexts.RouteEntry;
import com.tpservers.Routes.RouteMiddleware.RequestMiddleware;
import com.tpservers.Routes.RouteMiddleware.RequestReader;

public final class RouteDispatcher {

    private RouteDispatcher() {
    }

    private static class Holder {
        static boolean started = false;

        private static final RouteDispatcher INSTANCE = new RouteDispatcher();
    }

    public static RouteDispatcher getInstance() {
        return Holder.INSTANCE;
    }

    public static void boot() {

        if (Holder.started == true) {
            Console.error("Routes already started");
            return;
        }

        post("/api/command/:routeCommandTitle/:hostId/:routeCommand", (req, res) -> {
            
            try {
                JsonObject root = new JsonObject();

                RequestMiddleware.bodyValid(root, req, res);
                RequestMiddleware.paramValid(root, ":hostId", req, res);
                RequestMiddleware.paramValid(root, ":routeCommandTitle", req, res);
                RequestMiddleware.paramValid(root, ":routeCommand", req, res);
                RequestReader.boot(root, req);

                if (root.has("error")) {
                    res.status(400);
                    return ResponseService._400(root);
                }

                String routeCommandTitle = req.params(":routeCommandTitle");
                String routeCommand = req.params(":routeCommand");

                root.addProperty("route_host", req.params(":hostId"));
                root.addProperty("route_title", routeCommandTitle);
                root.addProperty("route_command", routeCommand);

                
                RouteEntry routeEntry = RouteRegistry.get(routeCommandTitle, routeCommand);

                if (routeEntry == null) {
                    res.status(404);
                    return ResponseService._404(root);
                }

                String jobName = routeCommandTitle + ":" + routeCommand + ":" + String.valueOf(Console.now());

                PoolService.submitJob(jobName, () -> {

                    try {

                        if (routeCommandTitle.equals("power") && routeCommand.equals("power-on")) {
                            Console.info("Hello");
                            routeEntry.routeHandle().invoke(null, root, req.params(":hostId"));
                        } else {
                            
                            /* Này người anh em, tôi cũng không biết sao cú pháp xài một method từ một record lại như thế này, để tôi tìm hiểu lý thuyết huhu */
                            routeEntry.routeHandle().invoke(null, root);
                        }
                    } catch (Exception e) {
                        Console.error(e.getMessage());
                    }
                });

                res.status(200);
                return ResponseService._200();
            } catch (Exception e) {
                Console.error(e.getMessage());
                res.status(500);
                return ResponseService._500(e.getMessage());
            }
        });
    }

    private static void requestJedisHandler(JsonObject root) {

        String key_prefix = "watcher:facade:request:";
        String key_suffix = "";

        if (root.has("route_command") && root.has("route_title")) {
            key_suffix = root.get("route_command").getAsString() + ":" + root.get("route_title").getAsString();
        }

        String redisKey = key_prefix + key_suffix;
        Console.info("API called info cached at redis key (1 hours expire): " + redisKey);

        try (Jedis jedis = RedisService.getResource()) {
            jedis.lpush(redisKey, new Gson().toJson(root));
            jedis.expire(redisKey, 3600);
        }
    }

}
