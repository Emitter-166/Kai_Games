package org.example.trueOrFalse;


import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class correct_answers extends ListenerAdapter {
    public static  String true_or_false_channel_id  = "";
    public static boolean answer = false;
    public static boolean isRunning = false;
    public static List<String> userIds = new ArrayList<>();
    public static boolean isRepeating = false;
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        String raw = e.getMessage().getContentRaw();
        if(raw.equalsIgnoreCase("true or false repeat")){
            if(e.getMember().hasPermission(Permission.MODERATE_MEMBERS)) {
                e.getMessage().reply("true or false").queue(
                        message -> message.delete().queue()
                );
                isRepeating = true;
                e.getMessage().reply("`Repeating started!` please do `true or false stop` to stop.")
                        .queue(message ->{
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            message.delete().queue();
                        });
            }else{
                e.getMessage().reply("`You don't have permissions to do that!`").queue(
                        message -> {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            message.delete().queue();
                        }
                );
            }
            return;
        }else if(raw.equalsIgnoreCase("true or false stop")){
            if(e.getMember().hasPermission(Permission.MODERATE_MEMBERS)) {
                isRepeating = false;
                e.getMessage().reply("`Repeating stopped!`")
                        .queue(message ->{
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            message.delete().queue();
                        });
            }else{
                e.getMessage().reply("`You don't have permissions to do that!`").queue(
                        message -> {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            message.delete().queue();
                        }
                );
            }
            return;
        }
        if (!isRunning) return;
        if(e.getAuthor().isBot()) return;
        if(!e.getChannel().getId().equalsIgnoreCase(true_or_false_channel_id)) return;

        if(raw.split(" ").length == 1 && (raw.equalsIgnoreCase("true") || raw.equalsIgnoreCase("false"))){
            if(Boolean.parseBoolean(raw) == answer){
                if (!userIds.contains(e.getAuthor().getId())){
                    userIds.add(e.getAuthor().getId());
                }
            }

        }
    }
}
