package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.example.trueOrFalse.capture_answer;
import org.example.trueOrFalse.trueOrFalse;
import org.example.truthOrDare.truthOrDare;


import javax.security.auth.login.LoginException;

public class Main {
    public static JDA jda;
    public static void main(String[] args) throws LoginException, InterruptedException {
        jda = JDABuilder.createLight(tokens.token)
                .addEventListeners(new truthOrDare())
                .addEventListeners(new trueOrFalse())
                .addEventListeners(new capture_answer())
                .build().awaitReady();

        String serverId = tokens.test_serverId;
        jda.getGuildById(serverId).upsertCommand("help", "shows commands for Kai Games").queue();
        jda.getGuildById(serverId).upsertCommand("truth", "sends a truth question!").queue();
        jda.getGuildById(serverId).upsertCommand("dare", "sends a dare question!").queue();
        jda.getGuildById(serverId).upsertCommand("wouldyourather", "sends a would you rather question!").queue();
        jda.getGuildById(serverId).upsertCommand("paranoia", "sends paranoia question!").queue();
        jda.getGuildById(serverId).upsertCommand("neverhaveiever", "sends never have I ever question!").queue();

        jda.getGuildById(serverId).upsertCommand("true-or-false", "sends a true or false question!").queue();
        jda.getGuildById(serverId).upsertCommand("true-or-false-repeat", "repeats sending true or false question!")
                .addOption(OptionType.INTEGER, "times", "amount of times you want to send", true)
                .queue();
        jda.getGuildById(serverId).upsertCommand("true-or-false-end", "stops sending question ").queue();



    }
}