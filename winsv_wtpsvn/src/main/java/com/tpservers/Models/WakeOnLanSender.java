package com.tpservers.Models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpservers.Services.Facade.ConfigService;
import tungtt.Console.Console;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Map;

public final class WakeOnLanSender {

    private WakeOnLanSender() {}

    private static class Holder {

        static final File HOST_MAP_FILE;
        static Map<String, String> HOST_MAP_CACHE;

        static {
            String fileName = ConfigService.LAN_HOST_MAP_FILE();

            HOST_MAP_FILE = resolveEnvDirFile(fileName);

            ensureFileExists();
            
            loadHostMap();
        }

        private static void ensureFileExists() {
            try {
                if (!HOST_MAP_FILE.exists()) {
                    HOST_MAP_FILE.createNewFile();
                    try (FileWriter w = new FileWriter(HOST_MAP_FILE)) {
                        w.write("{}");
                    }

                    Console.info(
                        "Created host map file next to .env:\n" +
                        HOST_MAP_FILE.getAbsolutePath()
                    );
                } else {
                    Console.info(
                        "Using host map file at:\n" +
                        HOST_MAP_FILE.getAbsolutePath()
                    );
                }
            } catch (Exception e) {
                throw new RuntimeException(
                    "Failed to create host map file",
                    e
                );
            }
        }
    }

    /* ===================== PUBLIC API ===================== */

    public static String ipResolver(String agentId) {

        if (agentId == null) return null;

        String ip = Holder.HOST_MAP_CACHE.get(agentId);

        if (ip == null) {
            Console.error(
                "Can not resolve IP for agentId: " + agentId
            );
        }

        return ip;
    }

    public static String getMapFilePath() {
        return Holder.HOST_MAP_FILE.getAbsolutePath();
    }

    /* ===================== INTERNAL ===================== */

    private static void loadHostMap() {

        try (FileReader reader = new FileReader(Holder.HOST_MAP_FILE)) {

            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>(){}.getType();

            Map<String, String> map = gson.fromJson(reader, type);
            Holder.HOST_MAP_CACHE = (map != null) ? map : Map.of();

            Console.info(
                "Loaded host map entries: " +
                Holder.HOST_MAP_CACHE.size()
            );

        } catch (Exception e) {
            Console.error(
                "Can not load host map file\n" +
                "Error: " + e.getMessage()
            );
            Holder.HOST_MAP_CACHE = Map.of();
        }
    }

    /**
     * Resolve file next to .env (working directory)
     */
    private static File resolveEnvDirFile(String fileName) {
        File workDir = new File(System.getProperty("user.dir"));
        return new File(workDir, fileName);
    }
}
