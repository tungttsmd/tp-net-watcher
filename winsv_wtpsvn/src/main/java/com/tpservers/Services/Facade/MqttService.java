package com.tpservers.Services.Facade;

import java.util.Set;
import java.lang.String;

import tungtt.Broker.Mqtt.Core.MqttCore;
import tungtt.Broker.Mqtt.Configs.MqttInit;
import tungtt.Broker.Mqtt.Facade.MqttFacade;
import tungtt.Broker.Mqtt.Interfaces.MqttMessageInterface;
import tungtt.Console.Console;

public final class MqttService {

    private MqttService() {
    }

    private static class Holder {
        static String CLIENT_ID;
        static MqttFacade mqtt;

        static final Set<String> SUB_TOPICS = Set.of(

                ConfigService.RUNTIME_TOPIC(),
                ConfigService.SENSOR_TOPIC(),
                ConfigService.PROFILE_TOPIC(),
                ConfigService.SYSTEM_TOPIC(),
                ConfigService.HEALTH_TOPIC(),
                ConfigService.SIGNAL_TOPIC());
    }

    public static void start() {

        Holder.CLIENT_ID = ConfigService.HOST_FROM_PREFIX() + "-" + ConfigService.HOST_ID() + "-"
                + HardwareService.hwHwid();

        MqttInit config = new MqttInit(
                ConfigService.MQTT_BROKER_URL(),
                Holder.CLIENT_ID);

        try {

            Holder.mqtt = MqttCore.start(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (String subTopic : Holder.SUB_TOPICS) {

            try {
                Holder.mqtt.subscribe(subTopic);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void onMessage(MqttMessageInterface handler) {

        if (Holder.mqtt == null) {

            Console.error("Mqtt client chưa được khởi tạo");
            throw new RuntimeException("onMessage error - MqttService");
        }

        Holder.mqtt.onMessage(handler);
    }

    public static void publish(String topic, String payload, int qos) {

        if (Holder.mqtt == null) {

            Console.error("Mqtt client chưa được khởi tạo");
            throw new RuntimeException("onMessage error - MqttService");
        }

        try {

            Holder.mqtt.publish(topic, payload);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String clientId() {

        if (Holder.CLIENT_ID == null) {

            Console.error("Mqtt client chưa được khởi tạo");
            throw new RuntimeException("MqttService is not booted");
        }

        return Holder.CLIENT_ID;
    }
}
