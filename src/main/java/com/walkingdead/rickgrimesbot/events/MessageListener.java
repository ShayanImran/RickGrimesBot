package com.walkingdead.rickgrimesbot.events;

import com.walkingdead.rickgrimesbot.BotConfiguration;
import com.walkingdead.rickgrimesbot.ChatGPT;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Random;

public abstract class MessageListener {

    private static final Logger log = LoggerFactory.getLogger(BotConfiguration.class);

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
            "We're still here. The last of us.",
            "We're not too far gone.",
            "They're screwing with the wrong people.",
            "You're not seeing things. You're just watching your friends die.",
            "We gotta start over. Start from scratch. No matter what comes next, we've won.",
            "I'm not who I was. Neither is Carl. You're not who you were. None of us are. We've all done worse kinds of things than we thought we could. And we're still here. The question is, how far are you willing to go?",
            "I'm looking for my wife and son. A little girl with a hat. You seen anyone like that?",
            "The world we know is gone, but keeping our humanity? That's a choice.",
            "I'm still a cop, and I'm still gonna find my family.",
            "Good people, they always die. And the bad people do, too. But the weak people, the people like me... we have inherited the Earth.",
            "No more kid stuff. People are gonna die. I'm gonna die. Mom... there's no way you can ever be ready for it.",
            "I'm not gonna bury you, Dad. I'm gonna remember you.",
            "You step outside, you risk your life. You take a drink of water, you risk your life. Nowadays you breathe and you risk your life. You don't have a choice. The only thing you can choose is what you're risking it for.",
            "You fight it. You don't give up. And one day, you just change.",
            "The world isn't what you think it is. It's much bigger, and it's much scarier.",
            "You don't get to come back. Not after what you did.",
            "There's a vast ocean of shit you people don't know shit about. Rick knows every fine grain of said shit... and then some.",
            "You're not that guy anymore, the guy who saves everybody.",
            "You're gonna rot in a cell in Alexandria.",
            "You're my brother.",
            "You're not gonna find them because they're not lost.",
            "It's not just about getting by anymore. It's about getting it all.",
            "This is what life looks like now. We have to fight it.",
            "You can't just be the good guy and expect to live, okay? Not anymore.",
            "You're my brother, but you're not good for anybody.",
            "Your mercy, your way of doing things, it's gonna get you killed.",
            "You're not saving the world, Rick. You're just getting it ready for me.",
            "My mercy prevails over my wrath.",
            "I can't sacrifice the greater good because it's hard.",
    };

    public Mono<Void> processCommand(Message eventMessage) {
        ChatGPT GPT = new ChatGPT();
        String reply = null;
        String inputMessage = eventMessage.getContent();
        log.info("Input message is: " + inputMessage);

        try {
            reply = GPT.sendMessageToChatGPT("Pretend you are Rick Grimes from the walking dead answering this question, " +
                    "reply without any fluff before the reply: " + inputMessage);
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

    private String getRandomQuote() {
        Random random = new Random();
        int randomIndex = random.nextInt(QUOTES.length);
        log.info("Quote is: " + QUOTES[randomIndex]);
        return QUOTES[randomIndex];
    }
}
