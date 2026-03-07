package com.tpservers.Routes.RouteMiddleware;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import spark.Request;
import spark.Response;

public class RequestMiddleware {

    public static void bodyValid(JsonObject root, Request req, Response res) {
        if (root.has("error")) {
            return;
        }
        // 1. Lấy body
        String body = req.body();

        // 2. Xử lý trường hợp body rỗng
        if (body == null || body.trim().isEmpty()) {
            res.status(400);
            res.type("application/json");
            root.addProperty("error", "Request body không được để trống");
            return;
        }

        // 2. Xử lý trường hợp body không hợp lệ
        try {
            root = JsonParser.parseString(body).getAsJsonObject();
        } catch (Exception e) {
            res.status(400);
            res.type("application/json");
            root.addProperty("error", "Dữ liệu JSON không hợp lệ");
            return;
        }

    }

    public static void paramValid(JsonObject root, String paramString, Request req, Response res) {
        if (root.has("error")) {
            return;
        }

        if (paramString.startsWith(":")) {
            paramString = paramString.substring(1); // remove ":" if user accidentally added
        }

        String paramValue = req.params(paramString); // Kiểm tra param có hợp lệ hay không

        if (paramValue == null || paramValue.isEmpty()) {
            res.status(400);
            root.addProperty("error", "param: [:" + paramString + "] is required on path");
            return;
        }
    }

    public static void headerValid(JsonObject root, Request req, Response res) {
        if (root.has("error")) {
            return;
        }
        // Chưa viết nội dung kiểm tra header ở đây
    }
}
