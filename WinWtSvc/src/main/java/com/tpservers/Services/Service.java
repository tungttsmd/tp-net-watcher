package com.tpservers.Services;

import com.tpservers.Services.Facade.ConfigService;
import com.tpservers.Services.Facade.MqttService;
import com.tpservers.Services.Facade.PortService;
import com.tpservers.Services.Facade.RedisService;
import com.tpservers.Services.Facade.PoolService;

import tungtt.Console.Console;

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

        if (Holder.started) {
            Console.error("Services already started");
            return;
        }

        Console.info("════════════════════════════════════════");
        Console.info("  tp-net-watcher  |  booting...");
        Console.info("════════════════════════════════════════");
        Console.line();

        Console.info("[DEV Note] The methods for handling service retrying faulty services are located in the error handling section of each Facade Service (source code)");
        Console.info("[DEV Note] Please do not spaghetti developing the methods for retrying faulty services to any location other than the Facade Service (source code)");
        Console.info("[DEV Note] Host service always need to be set up once before launch, don't give host any reboot app methods than faulty retrying is acceptable.");
        Console.line();

        /* ========== REDIS ============ */
        Console.info("[1/4] Starting Redis Service...");
        RedisService.start();
        Console.info("[1/4] Redis Service — OK  (" + ConfigService.REDIS_SERVER_HOST() + ":" + ConfigService.REDIS_SERVER_PORT() + ")");
        Console.line();

        /* ========== MQTT ============ */
        Console.info("[2/4] Starting MQTT Service...");
        MqttService.start();
        Console.info("[2/4] MQTT Service — OK");
        Console.line();

        /* ========== WORKER POOL ============ */
        Console.info("[3/4] Starting Worker Pool...");
        PoolService.start();
        Console.info("[3/4] Worker Pool — OK  (workers: " + PoolService.getWorkerCount() + ")");
        Console.line();

        /* ========== API PORT ============ */
        Console.info("[4/4] Starting API Service...");
        PortService.start();
        Console.info("[4/4] Webserver API Service — OK  (port: " + PortService.getPort() + ")");
        Console.line();

        /* ========== SUMMARY ============ */
        Console.info("════════════════════════════════════════");
        Console.info("  MQTT CLIENT ID  : " + MqttService.clientId());
        Console.info("  REDIS           : " + ConfigService.REDIS_SERVER_HOST() + ":" + ConfigService.REDIS_SERVER_PORT());
        Console.info("  API PORT        : " + PortService.getPort());
        Console.info("  THREADS         : " + PoolService.getWorkerCount());
        Console.info("════════════════════════════════════════");
        Console.line();

        Holder.started = true;
    }
}