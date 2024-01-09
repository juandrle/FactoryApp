package de.swtpro.factorybuilder.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class FrontendMessageService {

    private final SimpMessagingTemplate messaging;
    Logger LOGGER = LoggerFactory.getLogger(FrontendMessageService.class);

    FrontendMessageService(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    public void sendEvent(FrontendMessageEvent e, long factoryID) {
        messaging.convertAndSend("/info/factory/" + factoryID, e);
        LOGGER.info(
                String.format("Nachricht verschickt: %s %s[id=%d]", e.getOperationType(), e.getEventType(),
                        e.getEventID()));

    }


}
