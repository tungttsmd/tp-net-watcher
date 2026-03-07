package com.tpservers.Services.Facade;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ResponseService {
    private ResponseService() {
    }

    private static class Holder {
        private static final String STATUS_200 = "queued";
        private static final String STATUS_400 = "failed";
        private static final String STATUS_404 = "failed";
        private static final String STATUS_500 = "failed";
        private static final String ERROR_400 = "bad_request";
        private static final String ERROR_404 = "not_found";
        private static final String ERROR_500 = "internal_error";

        private static final boolean DEBUG_MODE_RESPONSE_CODE = Boolean
                .parseBoolean(System.getenv("DEBUG_MODE_RESPONSE_CODE"));

        private static final ResponseService INSTANCE = new ResponseService();

    }

    public static ResponseService getInstance() {
        return Holder.INSTANCE;
    }

    public static String _200() {
        JsonObject root = new JsonObject();
        root.addProperty("status", Holder.STATUS_200);
        root.addProperty("code", "200");
        if (Holder.DEBUG_MODE_RESPONSE_CODE) {
            root.addProperty("debug", root.get("code").getAsString());
        }
        return new Gson().toJson(root);
    }

    public static String _400(JsonObject root) {
        root.addProperty("status", Holder.STATUS_400);
        root.addProperty("error", Holder.ERROR_400);
        root.addProperty("code", "400");
        if (Holder.DEBUG_MODE_RESPONSE_CODE) {
            root.addProperty("debug", root.get("error").getAsString());
        }
        return new Gson().toJson(root);
    }

    public static String _404(JsonObject root) {
        root.addProperty("status", Holder.STATUS_404);
        root.addProperty("error", Holder.ERROR_404);
        root.addProperty("code", "404");
        if (Holder.DEBUG_MODE_RESPONSE_CODE) {
            root.addProperty("debug", root.get("error").getAsString());
        }
        return new Gson().toJson(root);
    }

    public static String _500(String message) {
        System.out.println("[ERROR] " + message);
        System.out.println("[INFO] Holder.DEBUG_MODE_RESPONSE_CODE: " + Holder.DEBUG_MODE_RESPONSE_CODE);
        JsonObject root = new JsonObject();
        root.addProperty("status", Holder.STATUS_500);
        root.addProperty("error", Holder.ERROR_500);
        root.addProperty("code", "500");
        if (Holder.DEBUG_MODE_RESPONSE_CODE) {
            root.addProperty("debug", message);
        }
        return new Gson().toJson(root);
    }
}
