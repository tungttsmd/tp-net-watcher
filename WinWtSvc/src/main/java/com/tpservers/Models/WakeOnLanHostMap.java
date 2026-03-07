package com.tpservers.Models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpservers.Services.Facade.ConfigService;
import tungtt.Console.Console;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;

public final class WakeOnLanHostMap {

    private WakeOnLanHostMap() {}

    private static class Holder {

        static final File FILE;
        static HashMap<String, String> CACHE;
        static final HashMap<String, String> HOST_ID_INDEX = new HashMap<>();

        static {
            FILE = resolveEnvDirFile(ConfigService.LAN_HOST_MAP_FILE());
            ensureFileExists();
            load();
        }

        private static void ensureFileExists() {
            try {
                if (!FILE.exists()) {
                    FILE.createNewFile();
                    try (FileWriter w = new FileWriter(FILE)) {
                        w.write("{}");
                    }
                    Console.info("Created host map file: " + FILE.getAbsolutePath());
                } else {
                    Console.info("Using host map file: " + FILE.getAbsolutePath());
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to create host map file", e);
            }
        }

        private static void load() {
            try (FileReader reader = new FileReader(FILE)) {
                Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                HashMap<String, String> map = new Gson().fromJson(reader, type);
                CACHE = (map != null) ? map : new HashMap<>();
                CACHE.forEach((nodeFrom, ip) -> {
                    String hid = extractHostId(nodeFrom);
                    if (hid != null) HOST_ID_INDEX.put(hid, ip);
                });
                Console.info("Loaded host map entries: " + CACHE.size());
            } catch (Exception e) {
                Console.error("Cannot load host map: " + e.getMessage());
                CACHE = new HashMap<>();
            }
        }

        private static File resolveEnvDirFile(String fileName) {
            try {
                String jarDir = java.nio.file.Paths.get(
                    WakeOnLanHostMap.class.getProtectionDomain()
                        .getCodeSource().getLocation().toURI()
                ).getParent().toString();
                return new File(jarDir, fileName);
            } catch (Exception e) {
                return new File(fileName);
            }
        }
    }

    public static String resolveIpByHostId(String hostId) {
        if (hostId == null) return null;
        String ip = Holder.HOST_ID_INDEX.get(hostId);
        if (ip == null) Console.error("Cannot resolve IP for hostId: " + hostId);
        return ip;
    }

    public static synchronized boolean registerIfAbsent(String nodeFrom, String ip) {
        if (nodeFrom == null || ip == null) return false;
        if (Holder.CACHE.containsKey(nodeFrom)) return false;

        Holder.CACHE.put(nodeFrom, ip);

        String hid = extractHostId(nodeFrom);
        if (hid != null) Holder.HOST_ID_INDEX.put(hid, ip);

        try (FileWriter w = new FileWriter(Holder.FILE)) {
            new Gson().toJson(Holder.CACHE, w);
        } catch (Exception e) {
            Console.error("Failed to persist host map: " + e.getMessage());
        }

        Console.info("Registered new host: " + nodeFrom + " -> " + ip);
        return true;
    }

    public static String extractLocalIp(String nodeFrom) {
        if (nodeFrom == null) return null;
        int idx = nodeFrom.indexOf('-');
        if (idx < 0) return null;
        String candidate = nodeFrom.substring(0, idx);
        return candidate.matches("\\d{1,3}(\\.\\d{1,3}){3}") ? candidate : null;
    }

    private static String extractHostId(String nodeFrom) {
        if (nodeFrom == null) return null;
        String[] parts = nodeFrom.split("-");
        for (int i = parts.length - 2; i >= 0; i--) {
            if (parts[i].matches("\\d+")) return parts[i];
        }
        return null;
    }
}
