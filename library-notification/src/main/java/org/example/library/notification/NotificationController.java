package org.example.library.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {

    @PostMapping("/send")
    public void sendNotification(@RequestBody NotificationRequest request) {
        log.info("--------------------------------------------------");
        log.info("SENDING NOTIFICATION (DUMMY)");
        log.info("To: {}", request.getTo());
        log.info("Subject: {}", request.getSubject());
        log.info("Body: {}", request.getBody());
        log.info("--------------------------------------------------");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationRequest {
        private String to;
        private String subject;
        private String body;
    }
}
