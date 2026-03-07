package com.tpservers.Services.Facade;

import java.time.Instant;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tpservers.Core.EventHandler;
import com.tpservers.Services.Facade.PoolService;

import tungtt.Console.Console;

public class EventService {

    private EventService() {

    }

    private static class Holder {

        private static final EventService INSTANCE = new EventService();
    }

    public static EventService getInstance() {
        return Holder.INSTANCE;
    }

    public static void boot() {

        Console.info("Event processor booted");

        MqttService.onMessage((topic, payload) -> handleEvent(topic, payload));
    }

    private static void handleEvent(String topic, byte[] payload) {

        JsonObject root;

        try {
            root = new Gson().fromJson(
                new String(payload, StandardCharsets.UTF_8),
                JsonObject.class
            );
        } catch (Exception e) {
            Console.error("Invalid JSON payload from topic: " + topic);
            return;
        }

        long ts = System.currentTimeMillis();
        root.addProperty("timestamp", ts);
        root.addProperty("job_name", topic);

        submitEvent(root, topic);
    }

    private static void submitEvent(JsonObject payload, String topic) {

        PoolService.submitJob(topic, () -> {

            EventHandler.handle(payload, topic);
        });
    }
}