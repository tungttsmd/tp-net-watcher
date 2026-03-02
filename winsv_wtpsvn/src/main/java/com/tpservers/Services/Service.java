package com.tpservers.Services;

import com.tpservers.Services.Facade.ConfigService;
import com.tpservers.Services.Facade.MqttService;
import com.tpservers.Services.Facade.PortService;
import com.tpservers.Services.Facade.RedisService;
import com.tpservers.Services.Facade.PoolService;

import tungtt.Console.Console;
import tungtt.Console.JsonConsole;

import tungtt.HardwareProfile.Modules.WindowsHardwareProfile;
import tungtt.HardwareProfile.Contexts.OsContext;
import tungtt.HardwareProfile.Interfaces.HardwareProfileProvider;

import oshi.SystemInfo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public final class Service {

    private Service() {
    }

    private static class Holder {

        static boolean started = false;

        static final Service INSTANCE = new Service();
    }

    public static Service getInstance() {

        return Holder.INSTANCE;
    }

    public static void boot() {

        if (Holder.started == true) {
            Console.error("Services already started");
            return;
        }

        Console.info("Booting services...");
        Console.line();

        HardwareProfileProvider hwProfile = new WindowsHardwareProfile(new OsContext(new SystemInfo()));

        /* ======================== TURN REDIS SERVICE ON ======================== */
        try {
            RedisService.start();
            Console.success("Redis host: " + ConfigService.REDIS_SERVER_HOST() + "\n" +
                    "Redis port: " + ConfigService.REDIS_SERVER_PORT() + "\n" +
                    "[1/5] Redis Service booted successfully");
        } catch (Exception e) {
            Console.error("[1/5] Redis Service booted failed");
        }
        Console.line();

        /* ======================== TURN MQTT SERVICE ON ======================== */
        try {
            MqttService.start();
            
            Console.info("MQTT Client ID: " + MqttService.clientId() + "\n" +
                    "[1/2] MQTT Service booted successfully");
        } catch (Exception e) {
            Console.error("[1/2] MQTT Service booted failed");
        }
        Console.line();

        /* ======================== TURN WORKER SERVICE ON ======================== */

        try {
            PoolService.start();
            Console.success("Worker pool count: " + PoolService.getWorkerCount() + "\n"
                    + "[2/2] Worker pool service booted successfully");
        } catch (Exception e) {
            Console.error("[2/2] Worker pool service booted failed");
        }

        Console.line();

        /* ======================== TURN API SERVICE ON ======================== */

        try {
            PortService.start();
            if (PortService.getPort() == 0) {
                throw new RuntimeException("Port connect failed");
            }
            Console.success("API Service running at port " + PortService.getPort() + "\n" +
                    "[5/5] API Service booted successfully");
        } catch (Exception e) {
            Console.error("[5/5] API Service booted failed");
        }
        Console.line();

        /* ======================== SERVICES BOOTED ======================== */

        Console.info("Services booted successfully");
        Holder.started = true;
        return;
    }
}