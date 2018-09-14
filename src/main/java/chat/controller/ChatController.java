package chat.controller;

import chat.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {

    @MessageMapping("/messages/{sala}")
    public Message send(Message message) {

        String time = new SimpleDateFormat("HH:mm").format(new Date());
        message.setTime(time);

        return message;
    }
}
