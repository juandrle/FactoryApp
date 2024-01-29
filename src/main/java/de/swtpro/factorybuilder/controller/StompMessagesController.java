package de.swtpro.factorybuilder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
// import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class StompMessagesController {

    
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    //mapped as /factory/messaging
    @MessageMapping("/messaging")
    @SendTo("/info/{factoryId}")
    public Message send(final Message message) throws Exception{
        return message; 
    }

    

    //mapped as factory/private
}




// wenn factory view geladen wird, soll man direkt den stomp messager registrieren, damit man 
// benachrichtigt wird, wenn änderungen in der factory passieren
// -> damit allen usern egal ist wer da noch so ist, die sollen sich einfach da reinhängen, damit
// sich die factory updated 
