package com.tpservers.Core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.tpservers.Models.WakeOnLanHostMap;
import com.tpservers.Services.Facade.ConfigService;
import com.tpservers.Services.Facade.ConsoleService;
import com.tpservers.Services.Facade.RedisService;

import tungtt.Console.Console;

import redis.clients.jedis.Jedis;

public class EventHandler {

    public static void handle(JsonObject root, String topic) {

        if (!middleware(root)) {
            Console.error(root.get("error").getAsString());
            return;
        }

        JsonObject meta = root.getAsJsonObject("meta");
        JsonObject data = root.getAsJsonObject("data");

        String hostId = meta.get("host_id").getAsString();
        long timestamp = meta.get("timestamp").getAsLong();
        String type = meta.get("json_response").getAsString();

        try (Jedis jedis = RedisService.getResource()) {

            switch (type) {

                case "heartbeat" ->
                    handleHeartbeat(jedis, meta, data, hostId, timestamp);

                case "signal" ->
                    handleSignal(jedis, meta, data, hostId, timestamp);

                case "report" ->
                    handleReport(jedis, meta, data, hostId, timestamp);

                default ->
                    Console.error("Unknown json_response: " + type);
            }

        } catch (Exception e) {
            Console.error("Redis error: " + e.getMessage());
        }
    }

    private static void handleHeartbeat(
        Jedis jedis,
        JsonObject meta,
        JsonObject data,
        String hostId,
        long ts
    ) {

        if (!data.has("heartbeatType")) return;

        JsonObject store = base(meta, hostId, ts);
        store.addProperty("heartbeatType", data.get("heartbeatType").getAsString());

        if (data.has("heartbeatNote")) {
            store.addProperty("heartbeatNote", data.get("heartbeatNote").getAsString());
        }

        String key = "event:heartbeat:"
            + store.get("heartbeatType").getAsString()
            + ":" + hostId;

        push(jedis, key, store);

        String nodeFrom = meta.get("node_from").getAsString();
        String registryKey = "event:host:seen:" + nodeFrom;

        if (!jedis.exists(registryKey)) {
            WakeOnLanHostMap.registerIfAbsent(nodeFrom, WakeOnLanHostMap.extractLocalIp(nodeFrom));
            jedis.set(registryKey, "1");
        }
    }

    private static void handleSignal(
        Jedis jedis,
        JsonObject meta,
        JsonObject data,
        String hostId,
        long ts
    ) {

        if (!data.has("signal_key")) return;

        JsonObject signalMetaObject = base(meta, hostId, ts);

        String signalKey = data.get("signal_key").getAsString();
        String signalContext = data.has("signal_context")
            ? data.get("signal_context").getAsString()
            : "{}";

        signalMetaObject.addProperty("signal_key", signalKey);
        signalMetaObject.addProperty("signal_context", signalContext);

        push(jedis, "event:signal:meta:" + hostId, signalMetaObject);


        // key list (wrap lại)
        JsonObject signalKeyObject = new JsonObject();

        signalKeyObject.addProperty("signal_key", signalKey);
        signalKeyObject.addProperty("timestamp", ts);

        push(jedis, "event:signal:key:" + hostId, signalKeyObject);
    }

    private static void handleReport(
        Jedis jedis,
        JsonObject meta,
        JsonObject data,
        String hostId,
        long ts
    ) {

        if (!data.has("report_title")
            || !data.has("report_handle")
            || !data.has("report_payload")) return;

        JsonObject store = base(meta, hostId, ts);
        store.addProperty("report_title", data.get("report_title").getAsString());
        store.addProperty("report_handle", data.get("report_handle").getAsString());
        store.addProperty("report_payload", data.get("report_payload").getAsString());

        String key = "event:report:"
            + store.get("report_title").getAsString()
            + ":"
            + store.get("report_handle").getAsString()
            + ":"
            + hostId;

        push(jedis, key, store);
    }


    private static JsonObject base(JsonObject meta, String hostId, long ts) {

        JsonObject o = new JsonObject();
        o.addProperty("host_id", hostId);
        o.addProperty("timestamp", ts);
        o.addProperty("json_response", meta.get("json_response").getAsString());
        o.addProperty("node_from", meta.get("node_from").getAsString());
        o.addProperty("node_version", meta.get("node_version").getAsString());
        return o;
    }

    private static void push(Jedis jedis, String key, JsonObject payload) {
        jedis.lpush(key, new Gson().toJson(payload));
        jedis.ltrim(key, 0, 999);
    }

    private static boolean middleware(JsonObject root) {

        if (!root.has("meta") || !root.has("data")) {
            root.addProperty("error", "Missing meta or data");
            return false;
        }

        JsonObject meta = root.getAsJsonObject("meta");

        if (!meta.has("host_id")
            || !meta.has("timestamp")
            || !meta.has("json_response")) {

            root.addProperty("error", "Invalid meta schema");
            return false;
        }

        long ts = meta.get("timestamp").getAsLong();
        long now = System.currentTimeMillis();

        // Anti replay ±10s
        if (Math.abs(now - ts) > 10_000) {
            root.addProperty("error", "Packet expired");
            return false;
        }

        return true;
    }
}
