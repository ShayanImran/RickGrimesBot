package com.walkingdead.rickgrimesbot.events;

import com.walkingdead.rickgrimesbot.BotConfiguration;
import com.walkingdead.rickgrimesbot.ChatGPT;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.Random;

public abstract class MessageListener {

    @Autowired
    private ChatGPT chatGPT;
    private static final Logger log = LoggerFactory.getLogger(BotConfiguration.class);

    public Mono<Void> processCommand(Message eventMessage) {
        String reply;
        String inputMessage = eventMessage.getContent();
        log.info("Input message is: " + inputMessage);

        try {
            reply = chatGPT.sendMessageToChatGPT("Reply as Rick Grimes from the walking dead, keep it only 1-2 sentences:" + inputMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String finalReply = reply;
        return Mono.just(eventMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                //.filter(message -> message.getContent().equalsIgnoreCase("!rick"))  commented for now until we can add a way to call the bot with a command for the message
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(finalReply))
                .then();
    }

}
