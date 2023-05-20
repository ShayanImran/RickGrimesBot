package com.walkingdead.rickgrimesbot.events;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import java.util.Random;

public abstract class MessageListener {

    private static final String[] QUOTES = {
            "We don't kill the living.",
            "This isn't a democracy anymore.",
            "I'm doing stuff, Lori. Things and stuff.",
            "I'm sorry this happened to you.",
            "We are the walking dead.",
            "You kill or you die. Or you die and you kill.",
            "We can't come back from the things we do.",
            "I'm not a good guy anymore.",
            "I'm still with you. Just do it.",
            "We're still here. The last of us."
    };

    public Mono<Void> processCommand(Message eventMessage) {
        return Mono.just(eventMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!carl"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(getRandomQuote()))
                .then();
    }

    private String getRandomQuote() {
        Random random = new Random();
        int randomIndex = random.nextInt(QUOTES.length);
        return QUOTES[randomIndex];
    }
}
