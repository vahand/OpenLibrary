package org.example.library.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.Map;

@Component
@Slf4j
public class NatsConsumer {

    @Value("${nats.url:nats://nats:4222}")
    private String natsUrl;

    private Connection natsConnection;
    private Dispatcher dispatcher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void subscribe() throws Exception {
        log.info("Connecting to NATS at: {}", natsUrl);

        Options options = new Options.Builder()
                .server(natsUrl)
                .build();

        natsConnection = Nats.connect(options);

        dispatcher = natsConnection.createDispatcher((msg) -> {
            try {
                String json = new String(msg.getData());
                @SuppressWarnings("unchecked")
                Map<String, String> event = objectMapper.readValue(json, Map.class);

                log.info("--------------------------------------------------");
                log.info("RECEIVED EVENT FROM NATS");
                log.info("Topic: {}", msg.getSubject());
                log.info("Event Type: {}", event.get("eventType"));
                log.info("To: {}", event.get("to"));
                log.info("Subject: {}", event.get("subject"));
                log.info("Body: {}", event.get("body"));
                log.info("--------------------------------------------------");

                // Here you would send the actual email
                // For now, just logging (dummy implementation)

            } catch (Exception e) {
                log.error("Failed to process event", e);
            }
        });

        dispatcher.subscribe("book.rented");
        dispatcher.subscribe("book.returned");

        log.info("Subscribed to topics: book.rented, book.returned");
    }

    @PreDestroy
    public void cleanup() {
        if (natsConnection != null) {
            try {
                natsConnection.close();
                log.info("NATS connection closed");
            } catch (Exception e) {
                log.error("Error closing NATS connection", e);
            }
        }
    }
}
