package chat.controller;

import chat.model.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {

    @MessageMapping("/messages/{sala}")
    public Message send(Message message) {

        String time = new SimpleDateFormat("HH:mm").format(new Date());
        message.setTime(time);

        throw new MessagingException("500");

//        return message;
    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String handleMessagingException(MessagingException exception) {

        return exception.getMessage();
    }
}
