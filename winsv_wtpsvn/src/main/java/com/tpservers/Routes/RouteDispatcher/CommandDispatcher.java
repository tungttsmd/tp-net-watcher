package com.tpservers.Routes.RouteDispatcher;

import tungtt.Console.Console;
import tungtt.Console.JsonConsole;
import tungtt.Envelope.Modules.CommandEnvelope;
import tungtt.Envelope.Contexts.*;
import com.tpservers.Services.Facade.ConfigService;
import com.tpservers.Services.Facade.MqttService;

public final class CommandDispatcher {

    private CommandDispatcher() {
    }

    public static void send(
            String hostIdentifier,
            String title,
            String command,
            String context) {

        try {

            // Lấy chuỗi target type "agent" hoặc "trigger" từ Host Identifier
            String[] parts = hostIdentifier.split("-");

            String targetType = parts[2]; // agent hoặc trigger
            String targetId = parts[3]; // Port
            String targetHwid = parts[4]; // chuỗi 8 số hwid

            CommandModuleContext dataContext = new CommandModuleContext(title, command, context);

            EnvelopeMetaContext metaContext = new EnvelopeMetaContext(
                    targetId,
                    ConfigService.HOST_FROM_PREFIX() + "-" + targetId + "-00000000",
                    ConfigService.HOST_VERSION(),
                    Console.now());

            EnvelopeSecureContext secureContext = new EnvelopeSecureContext("[coming soon]", "[coming soon]");

            CommandEnvelope enveloped = new CommandEnvelope(metaContext, secureContext, dataContext);

            String agentTopic = ConfigService.CLIENT_CONTROL_TOPIC()
                    .replace(":hostIdentifier", hostIdentifier);

            String triggerTopic = ConfigService.NODE_CONTROL_TOPIC()
                    .replace(":hostIdentifier", hostIdentifier);

            String payload = JsonConsole.toJson(enveloped.build());

            switch (targetType) {
                case "agent":
                    MqttService.publish(agentTopic, payload, 1);
                    break;
                case "trigger":
                    MqttService.publish(triggerTopic, payload, 1);
                    break;
                default:
                    Console.error("Can not resolve target type (target type valid: agent/trigger): " + targetType);
            }

        } catch (Exception e) {

            Console.error("Command dispatch failed: " + e.getMessage());

        }
    }
}
