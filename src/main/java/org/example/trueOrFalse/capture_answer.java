package org.example.trueOrFalse;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class capture_answer extends ListenerAdapter {
    public static Map<String, Integer> winners = new HashMap<>();
    public static List<String> temporary_winnersId = new ArrayList<>();
    public static List<String> answered = new ArrayList<>();
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if(!trueOrFalse.isRunning) return;
        if((!e.getChannel().getId().equalsIgnoreCase(trueOrFalse.channelId))) return;

        String message = e.getMessage().getContentRaw();
        if(message.equalsIgnoreCase("true") || message.equalsIgnoreCase("false")){
            if(!answered.contains(e.getAuthor().getId())){
                if(trueOrFalse.answer == Boolean.parseBoolean(e.getMessage().getContentRaw())){
                    temporary_winnersId.add(e.getAuthor().getId());
                    if(!winners.containsKey(e.getAuthor().getId())){
                        winners.put(e.getAuthor().getId(), 0);
                    }
                    winners.put(e.getAuthor().getId(), winners.get(e.getAuthor().getId()) + 1);
                }
                answered.add(e.getAuthor().getId());
            }else{
                e.getMessage().reply("`you already answered once!`").mentionRepliedUser(false).queue();
            }
        }
    }
}
