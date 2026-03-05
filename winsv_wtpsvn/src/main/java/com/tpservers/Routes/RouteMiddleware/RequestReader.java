package com.tpservers.Routes.RouteMiddleware;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import spark.Request;

public class RequestReader {
    public static JsonObject boot(JsonObject root, Request req) {
        if (root.has("error")) {
            return root;
        }

        try {
            root = header(root, req);
            root = body(root, req);
            return root;
        } catch (Exception e) {
            e.printStackTrace();
            root.addProperty("error", "exception on parsing request");
            System.out.println("[ERROR] Request - Exception on parsing request: " + e.getMessage());
            return root;
        }
    }

    public static JsonObject header(JsonObject root, Request req) {
        if (root.has("error")) {
            return root;
        }
        try {
            JsonObject headers = new JsonObject();
            for (String h : req.headers()) {
                headers.addProperty(h.toLowerCase(), req.headers(h));
            }
            root.add("header_read", headers);
            return root;
        } catch (Exception e) {
            e.printStackTrace();
            root.addProperty("error", "exception on parsing header");
            System.out.println("[ERROR] Request - Exception on parsing header: " + e.getMessage());
            return root;
        }
    }

    public static JsonObject body(JsonObject root, Request req) {
        if (root.has("error")) {
            return root;
        }
        try {
            String raw = req.body();

            JsonElement outer = JsonParser.parseString(raw);

            if (outer.isJsonPrimitive() && outer.getAsJsonPrimitive().isString()) {
                raw = outer.getAsString();
            }

            JsonObject payload = JsonParser.parseString(raw).getAsJsonObject();
            root.add("payload_read", payload);
            return root;
        } catch (Exception e) {
            e.printStackTrace();
            root.addProperty("error", "exception on parsing body");
            System.out.println("[ERROR] Request - Exception on parsing body: " + e.getMessage());
            return root;
        }
    }
}
