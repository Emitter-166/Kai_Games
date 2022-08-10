package org.example.trueOrFalse;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Main;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.trueOrFalse.trueOrFalse.*;

class operation implements Runnable{

    @Override
    public void run() {
        for(int i = 0; i < repeat; i++){
            answer = send(channelId);
            try {
                Thread.sleep(30_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try{
                sendResult(channelId, answer);
                sendLeaderboard(channelId);

            }catch (IllegalArgumentException exception){}
            capture_answer.temporary_winnersId = new ArrayList<>();
            capture_answer.answered = new ArrayList<>();

        }

        if(repeat != 1){
            Main.jda.getTextChannelById(channelId).sendMessage("`game over!`").queue();
        }
        capture_answer.winners = new HashMap<>();

        answer = false;
        channelId = "";
        isRunning = false;
        repeat = 0;
     }
}
public class trueOrFalse extends ListenerAdapter {
    public static boolean answer = false;
    public static String channelId = "";
    public static boolean isRunning = false; //this variable will signal capture_answer to capture messages
    public static int repeat = 0;
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e){
        String name = e.getName();
        Thread thread = null;
        Thread thread1 = null;
        switch (name){
            case "true-or-false":
                if(isRunning){
                    e.getInteraction().reply("`A game of true or false is already running!`").queue();
                    return;
                }
                e.getInteraction().reply("`Sending true or false...`").queue();
                isRunning = true;
                channelId = e.getChannel().getId();
                repeat = 1;

                thread1 = new Thread(new operation());
                thread1.start();

                break;
            case "true-or-false-repeat":
                if(isRunning){
                    e.getInteraction().reply("`A game of true or false is already running!`").queue();
                    return;
                }
                e.getInteraction().reply("`Sending true or false...`").queue();
                isRunning = true;
                channelId = e.getChannel().getId();
                repeat = e.getOption("times").getAsInt();

                 thread = new Thread(new operation());
                thread.start();

                break;

            case "true-or-false-end":
                e.getInteraction().reply("`Stopping true or false...`").queue();
                if(thread != null){
                    thread.interrupt();
                }
                if(thread1 != null){
                    thread1.interrupt();
                }


                sendResult(channelId, answer);
                sendLeaderboard(channelId);
                capture_answer.temporary_winnersId = new ArrayList<>();
                capture_answer.answered = new ArrayList<>();

                if(repeat != 1){
                    Main.jda.getTextChannelById(channelId).sendMessage("`game over!`").queue();
                    sendLeaderboard(channelId);
                }
                capture_answer.winners = new HashMap<>();

                answer = false;
                isRunning = false;
                channelId = "";
                repeat = 0;
                break;

        }

    }

    static boolean send(String channelId){
        HttpResponse<JsonNode> response;
        try {
            response = Unirest.get("https://opentdb.com/api.php?amount=1&type=boolean").asJson();
        } catch (UnirestException ex) {
            throw new RuntimeException(ex);
        }
        String question = ((JSONObject) (((JSONArray) response.getBody().getObject().get("results")).get(0))).get("question").toString().replace("&#039;", "'");
        boolean answer = Boolean.parseBoolean((String) ((JSONObject) (((JSONArray) response.getBody().getObject().get("results")).get(0))).get("correct_answer"));
        Main.jda.getTextChannelById(channelId).sendMessageEmbeds(new EmbedBuilder()
                .setTitle("True or False?")
                .setColor(Color.CYAN)
                .setDescription("**" + question.replace("&quot;", "\"") + "**")
                .build()).queue();
        return answer;
    }

    static void sendLeaderboard(String channelId){
        StringBuilder winners = new StringBuilder();
        List<Map.Entry<String, Integer>> sorted = capture_answer.winners.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toList());
        for(int i = 0; i < sorted.size(); i++){
           winners.append(String.format("`%s.` <@%s> **%s** points! \n", i+1, sorted.get(i).getKey(), sorted.get(i).getValue()));
        }
        Main.jda.getTextChannelById(channelId).sendMessageEmbeds(new EmbedBuilder()
                        .setTitle("points leaderboard")
                        .setDescription(winners)
                        .setColor(Color.WHITE)
                .build()).queue();
    }

    static void sendResult(String channelId, boolean answer){
        StringBuilder result = new StringBuilder();
        capture_answer.temporary_winnersId.stream().forEach(Id ->{
            result.append(String.format("<@%s> \n", Id));
        });
        TextChannel channel = Main.jda.getTextChannelById(channelId);
        if(result.toString().equals("")){
            result.append("No winners!");
        }
        channel.sendMessage(result.toString()).queue(message -> message.delete().queue());
        channel.sendMessageEmbeds(new EmbedBuilder()
                        .setTitle("Correct answer is " + answer + "!")
                        .addField("Winners: ", result.toString(), false)
                        .setColor(Color.WHITE)
                .build()).queue();
    }

}
