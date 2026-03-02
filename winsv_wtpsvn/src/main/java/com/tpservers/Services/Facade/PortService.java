package com.tpservers.Services.Facade;

import static spark.Spark.port;

public final class PortService {

    private PortService() {
    }

    private static class Holder {

        static boolean started = false;

        static final int PORT = ConfigService.APP_API_PORT();

        static final PortService INSTANCE = new PortService();
    }

    public static PortService getInstance() {
        return Holder.INSTANCE;
    }

    public static void start() {

        if (Holder.started == true) {
            ConsoleService.info("Port service already started");
            return;
        }
        ConsoleService.info("Connecting to port service...");
        port(Holder.PORT);
        ConsoleService.info("Port service connected");
        Holder.started = true;
    }

    public static int getPort() {
        return Holder.PORT;
    }
}
