package org.example.truthOrDare;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.IOException;

public class truthOrDare extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e){
        //help cmds
        String command = e.getName();
        if(command.equalsIgnoreCase("help")) {
            e.getInteraction().replyEmbeds(new EmbedBuilder()
                            .setTitle("Help commands for Kai Games!")
                            .setColor(Color.CYAN)
                            .addField("Help commands for truth or dare", "" +
                                    "`/truth` **get a truth question** \n" +
                                    "`/dare` **get a dare task** \n " +
                                    "`/wouldyourather` **get a would you rather question** \n" +
                                    "`/paranoia` **get a paranoia question** \n" +
                                    "`/neverhaveiever` **get a never have I ever question** \n", false)
                            .addField("Commands for true or false", "" +
                                    "`/true-or-false` **get a true or false question** \n" +
                                    "`/true-or-false-repeat times` **Repeats truth or false question every 30 seconds for a certain amount of times** \n" +
                                    "`/true-or-false-stop` **Stops repeating questions**", false)
                            .build())
                    .mentionRepliedUser(false)
                    .queue();
        }

        switch (command){
            case "truth":
                try {
                    e.getInteraction().replyEmbeds(new EmbedBuilder()
                                    .setTitle("Truth")
                                    .setColor(Color.WHITE)
                                    .setDescription(String.format("**%s**", getTruth()))
                                    .build())
                            .mentionRepliedUser(false)
                            .queue();
                } catch (IOException | UnirestException ex) {
                    throw new RuntimeException(ex);
                }
                break;

            case "dare":
                try {
                    e.getInteraction().replyEmbeds(new EmbedBuilder()
                                    .setTitle("Dare")
                                    .setColor(Color.WHITE)
                                    .setDescription(String.format("**%s**", getDare()))
                                    .build())
                            .mentionRepliedUser(false)
                            .queue();
                } catch (UnirestException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "wouldyourather":
                try {
                    e.getInteraction().replyEmbeds(new EmbedBuilder()
                                    .setTitle("Would you rather...")
                                    .setColor(Color.WHITE)
                                    .setDescription(String.format("**%s**", getWouldYouRather()))
                                    .build())
                            .mentionRepliedUser(false)
                            .queue();
                } catch (UnirestException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "paranoia":
                try {
                    e.getInteraction().replyEmbeds(new EmbedBuilder()
                                    .setTitle("Paranoia!")
                                    .setColor(Color.WHITE)
                                    .setDescription(String.format("**%s**", getParanoia()))
                                    .build())
                            .mentionRepliedUser(false)
                            .queue();
                } catch (UnirestException ex) {
                    throw new RuntimeException(ex);
                }
                break;

            case "neverhaveiever":
                try {
                    e.getInteraction().replyEmbeds(new EmbedBuilder()
                                    .setTitle("Never have you ever...")
                                    .setColor(Color.WHITE)
                                    .setDescription(String.format("**%s**", getNeverHaveIEver()))
                                    .build())
                            .mentionRepliedUser(false)
                            .queue();
                } catch (UnirestException ex) {
                    throw new RuntimeException(ex);
                }
                break;
        }
    }



    static String getTruth() throws IOException, UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("https://api.truthordarebot.xyz/v1/truth")
                .asJson();
        return (String) response.getBody().getObject().get("question");
    }

    static String getWouldYouRather() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("https://api.truthordarebot.xyz/api/wyr")
                .asJson();
        return (String) response.getBody().getObject().get("question");
    }

    static String getDare() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("https://api.truthordarebot.xyz/api/dare")
                .asJson();
        return (String) response.getBody().getObject().get("question");
    }

    static String getParanoia() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("https://api.truthordarebot.xyz/api/paranoia")
                .asJson();
        return (String) response.getBody().getObject().get("question");
    }

    static String getNeverHaveIEver() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("https://api.truthordarebot.xyz/api/nhie")
                .asJson();
        return (String) response.getBody().getObject().get("question");
    }
}

