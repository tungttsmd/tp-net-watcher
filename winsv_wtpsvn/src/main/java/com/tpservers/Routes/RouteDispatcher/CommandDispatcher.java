package com.tpservers.Routes.RouteDispatcher;

import tungtt.Console.Console;
import tungtt.Console.JsonConsole;
import tungtt.Envelope.Modules.CommandEnvelope;
import tungtt.Envelope.Contexts.*;
import com.tpservers.Services.Facade.ConfigService;
import com.tpservers.Services.Facade.MqttService;

public final class CommandDispatcher {

    private CommandDispatcher() {}

    public static void send(
        String hostId,
        String title,
        String command,
        String context
    ) {

        try {

            CommandModuleContext dataContext = new CommandModuleContext(title, command, context);
            EnvelopeMetaContext metaContext = new EnvelopeMetaContext(hostId, ConfigService.HOST_FROM_PREFIX() + "-" + hostId,ConfigService.HOST_VERSION(),Console.now());
            EnvelopeSecureContext secureContext = new EnvelopeSecureContext("[coming soon]","[coming soon]");

            CommandEnvelope enveloped = new CommandEnvelope(metaContext, secureContext, dataContext);

            String topic = ConfigService.CONTROL_TOPIC().replace(":hostId", hostId);
            String payload = JsonConsole.toJson(enveloped.build());

            MqttService.publish(topic, payload, 1);

        } catch (Exception e) {
            Console.error("Command dispatch failed: " + e.getMessage());
        }
    }
}
