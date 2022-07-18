package global.easycoding.events.activity;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class Help extends ListenerAdapter {



    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMessage().getContentStripped().equals("!help")) {
            EmbedBuilder help = new EmbedBuilder();
            help.setTitle("Help");
            help.setDescription("This lists all the commands you can use in this bot.");
            help.setColor(0x00ff00);
            help.addField("`!help`", "This lists all the commands you can use in this bot.", false);
            help.addField("`!betrayal`", "This will play Betrayal.io with your friends.", false);
            help.addField("`!chessinthepark`", "This will play Chess in the Park with your friends.", false);
            help.addField("`!fishington`", "This will play Fishington.io with your friends.", false);
            help.addField("`!pokernight`", "This will play Poker Night with your friends.", false);
            help.addField("`!youtube`", "This will play YouTube with your friends.", false);
            help.addField("WARNING!", "This Message will be deleted in 15 minutes", false);

            event.getChannel().sendMessageEmbeds(help.build()).queue(message -> {
                message.delete().queueAfter(15, TimeUnit.MINUTES);
            });
        }
    }
}
