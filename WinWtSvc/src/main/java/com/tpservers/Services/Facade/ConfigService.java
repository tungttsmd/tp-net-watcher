package com.tpservers.Services.Facade;

import com.tpservers.Repositories.MetaRespository;

public final class ConfigService {

    private ConfigService() {
    }

    private static class Holder {

        static final String prop(String key) {
            return System.getProperty(key);
        }

        static final String propReplaceHostId(String key) {
            String v = prop(key);
            if (v == null || v.isBlank()) {
                throw new IllegalStateException("Missing system property: " + key);
            }
            return v.replace(":hostId", String.valueOf(MetaRespository.hostId()));
        }

        static final String propReplaceHostIdentify(String key) {
            String v = prop(key);
            if (v == null || v.isBlank()) {
                throw new IllegalStateException("Missing system property: " + key);
            }
            String fromPrefix = prop("HOST_FROM_PREFIX") != null ? prop("HOST_FROM_PREFIX")
                    : "nullOn-" + MetaRespository.hostHwid();
            String deviceType = prop("HOST_DEVICE_TYPE") != null ? prop("HOST_DEVICE_TYPE")
                    : "nullOn-" + MetaRespository.hostHwid();
            return v
                    .replace(":hostFromPrefix", fromPrefix)
                    .replace(":hostId", String.valueOf(MetaRespository.hostId()))
                    .replace(":hostHwid", String.valueOf(MetaRespository.hostHwid()))
                    .replace(":hostDeviceType", deviceType);
        }

        static final int propIntParse(String key) {
            String v = prop(key);
            if (v == null || v.isBlank()) {
                throw new IllegalStateException("Missing system property: " + key);
            }
            try {
                return Integer.parseInt(v);
            } catch (NumberFormatException e) {
                throw new IllegalStateException(
                        "Invalid integer system property: " + key + " = " + v);
            }
        }

        /* =========================== ENV INDENTIFY =========================== */

        static final String HOST_VERSION = prop("HOST_VERSION");
        static final String HOST_FROM_PREFIX = prop("HOST_FROM_PREFIX");
        static final int HOST_ID = MetaRespository.hostId();

        /* ========================= ENV APP SETTING ========================= */

        static final int APP_API_PORT = propIntParse("APP_API_PORT");
        static final String LAN_HOST_MAP_FILE = prop("LAN_HOST_MAP_FILE");

        /* =========================== ENV REDIS =========================== */
        static final String REDIS_SERVER_HOST = prop("REDIS_SERVER_HOST");
        static final int REDIS_SERVER_PORT = propIntParse("REDIS_SERVER_PORT");

        static final int REDIS_POOL_THREAD_HEARTBEAT_EXPIRE = propIntParse("REDIS_POOL_THREAD_HEARTBEAT_EXPIRE");
        static final int REDIS_POOL_THREAD_HEARTBEAT_INTERVAL = propIntParse("REDIS_POOL_THREAD_HEARTBEAT_INTERVAL");

        static final String REDIS_JOB_PREFIX_NAME = prop("REDIS_JOB_PREFIX_NAME");
        static final String REDIS_POOL_NAMED_THREAD_PREFIX = prop("REDIS_POOL_NAMED_THREAD_PREFIX");
        static final String REDIS_POOL_WORKER_PREFIX = prop("REDIS_POOL_WORKER_PREFIX");

        static final int REDIS_POOL_CONFIG_MAX_TOTAL = propIntParse("REDIS_POOL_CONFIG_MAX_TOTAL");
        static final int REDIS_POOL_CONFIG_MAX_IDLE = propIntParse("REDIS_POOL_CONFIG_MAX_IDLE");
        static final int REDIS_POOL_CONFIG_MIN_IDLE = propIntParse("REDIS_POOL_CONFIG_MIN_IDLE");

        /* ============================ ENV MQTT ============================ */

        static final String MQTT_SERVER_HOST = prop("MQTT_SERVER_HOST");
        static final int MQTT_SERVER_PORT = propIntParse("MQTT_SERVER_PORT");

        static final String MQTT_OPT_USERNAME = prop("MQTT_OPT_USERNAME");
        static final String MQTT_OPT_PASSWORD = prop("MQTT_OPT_PASSWORD");

        // SUB
        static final String SUBSCRIBE_TOPIC = prop("MQTT_SUBSCRIBE_TOPIC");

        // PUB (raw templates — placeholders resolved at runtime per target machine)
        static final String CLIENT_CONTROL_TOPIC = prop("MQTT_CLIENT_CONTROL_TOPIC");
        static final String NODE_CONTROL_TOPIC = prop("MQTT_NODE_CONTROL_TOPIC");

        static final String SIGNAL_TARGET_KEY = prop("MQTT_SIGNAL_TARGET_KEY");

        /* ========================= ENV WORKER POOL ========================== */

        static final String POOL_WORKER_PREFIX = prop("REDIS_POOL_WORKER_PREFIX");
        static final String POOL_NAMED_THREAD_PREFIX = prop("REDIS_POOL_NAMED_THREAD_PREFIX");
        static final int POOL_WORKER_COUNT = propIntParse("REDIS_POOL_WORKER_COUNT");

        /* ========================= ENV JOB SERVICE ========================== */

        static final String REDIS_JOB_PREFIX_SET_NAME = prop("REDIS_JOB_PREFIX_SET_NAME");
        static final int REDIS_JOB_QUEUED_EXPIRED_TIME = propIntParse("REDIS_JOB_QUEUED_EXPIRED_TIME");
        static final int REDIS_JOB_PENDING_EXPIRED_TIME = propIntParse("REDIS_JOB_PENDING_EXPIRED_TIME");
        static final int REDIS_JOB_PROCESSING_EXPIRED_TIME = propIntParse("REDIS_JOB_PROCESSING_EXPIRED_TIME");
        static final int REDIS_JOB_DONE_EXPIRED_TIME = propIntParse("REDIS_JOB_DONE_EXPIRED_TIME");
        static final int REDIS_JOB_FAILED_EXPIRED_TIME = propIntParse("REDIS_JOB_FAILED_EXPIRED_TIME");

        static final ConfigService INSTANCE = new ConfigService();
    }

    public static ConfigService getInstance() {
        return Holder.INSTANCE;
    }

    /* ========================= ENV IDENTIFY ========================== */

    public static String HOST_VERSION() {
        return Holder.HOST_VERSION;
    }

    public static String HOST_FROM_PREFIX() {
        return Holder.HOST_FROM_PREFIX;
    }

    public static int HOST_ID() {
        return Holder.HOST_ID;
    }

    /* ========================= ENV APP SETTING ========================= */

    public static int APP_API_PORT() {
        return Holder.APP_API_PORT;
    }

    public static String LAN_HOST_MAP_FILE() {
        return Holder.LAN_HOST_MAP_FILE;
    }

    /* ========================= ENV REDIS ========================== */

    public static String REDIS_SERVER_HOST() {
        return Holder.REDIS_SERVER_HOST;
    }

    public static int REDIS_SERVER_PORT() {
        return Holder.REDIS_SERVER_PORT;
    }

    public static int REDIS_POOL_THREAD_HEARTBEAT_EXPIRE() {
        return Holder.REDIS_POOL_THREAD_HEARTBEAT_EXPIRE;
    }

    public static int REDIS_POOL_THREAD_HEARTBEAT_INTERVAL() {
        return Holder.REDIS_POOL_THREAD_HEARTBEAT_INTERVAL;
    }

    public static String REDIS_JOB_PREFIX_NAME() {
        return Holder.REDIS_JOB_PREFIX_NAME;
    }

    public static String REDIS_POOL_NAMED_THREAD_PREFIX() {
        return Holder.REDIS_POOL_NAMED_THREAD_PREFIX;
    }

    public static String REDIS_POOL_WORKER_PREFIX() {
        return Holder.REDIS_POOL_WORKER_PREFIX;
    }

    public static int REDIS_POOL_CONFIG_MAX_TOTAL() {
        return Holder.REDIS_POOL_CONFIG_MAX_TOTAL;
    }

    public static int REDIS_POOL_CONFIG_MAX_IDLE() {
        return Holder.REDIS_POOL_CONFIG_MAX_IDLE;
    }

    public static int REDIS_POOL_CONFIG_MIN_IDLE() {
        return Holder.REDIS_POOL_CONFIG_MIN_IDLE;
    }

    /* ========================= ENV MQTT ========================== */

    public static String MQTT_SERVER_HOST() {
        return Holder.MQTT_SERVER_HOST;
    }

    public static int MQTT_SERVER_PORT() {
        return Holder.MQTT_SERVER_PORT;
    }

    public static String MQTT_BROKER_URL() {
        return "tcp://" + Holder.MQTT_SERVER_HOST + ":" + Holder.MQTT_SERVER_PORT;
    }

    public static String MQTT_OPT_USERNAME() {
        return Holder.MQTT_OPT_USERNAME;
    }

    public static String MQTT_OPT_PASSWORD() {
        return Holder.MQTT_OPT_PASSWORD;
    }

    public static String SUBSCRIBE_TOPIC() {
        return Holder.SUBSCRIBE_TOPIC;
    }

    public static String CLIENT_CONTROL_TOPIC() {
        return Holder.CLIENT_CONTROL_TOPIC;
    }

    public static String NODE_CONTROL_TOPIC() {
        return Holder.NODE_CONTROL_TOPIC;
    }

    public static String SIGNAL_TARGET_KEY() {
        return Holder.SIGNAL_TARGET_KEY;
    }

    /* ========================= ENV WORKER POOL ========================== */

    public static String POOL_WORKER_PREFIX() {
        return Holder.POOL_WORKER_PREFIX;
    }

    public static String POOL_NAMED_THREAD_PREFIX() {
        return Holder.POOL_NAMED_THREAD_PREFIX;
    }

    public static int POOL_WORKER_COUNT() {
        return Holder.POOL_WORKER_COUNT;
    }

    /* ========================= ENV JOB SERVICE ========================== */

    public static String REDIS_JOB_PREFIX_SET_NAME() {
        return Holder.REDIS_JOB_PREFIX_SET_NAME;
    }

    public static int REDIS_JOB_QUEUED_EXPIRED_TIME() {
        return Holder.REDIS_JOB_QUEUED_EXPIRED_TIME;
    }

    public static int REDIS_JOB_PENDING_EXPIRED_TIME() {
        return Holder.REDIS_JOB_PENDING_EXPIRED_TIME;
    }

    public static int REDIS_JOB_PROCESSING_EXPIRED_TIME() {
        return Holder.REDIS_JOB_PROCESSING_EXPIRED_TIME;
    }

    public static int REDIS_JOB_DONE_EXPIRED_TIME() {
        return Holder.REDIS_JOB_DONE_EXPIRED_TIME;
    }

    public static int REDIS_JOB_FAILED_EXPIRED_TIME() {
        return Holder.REDIS_JOB_FAILED_EXPIRED_TIME;
    }

}
