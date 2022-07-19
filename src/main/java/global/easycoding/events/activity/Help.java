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
            help.addField("`!awkword`", "This will generate a game.", false);
            help.addField("`!betrayal`", "This will generate a game.", false);
            help.addField("`!blazingeight`", "This will generate a game.", false);
            help.addField("`!bobbleleague`", "This will generate a game.", false);
            help.addField("`!checkersinthepark`", "This will generate a game.", false);
            help.addField("`!chessinthepark`", "This will generate a game.", false);
            help.addField("`!doodlecrew`", "This will generate a game.", false);
            help.addField("`!fishington`", "This will generate a game.", false);
            help.addField("`!landio`", "This will generate a game.", false);
            help.addField("`!letterleague`", "This will generate a game.", false);
            help.addField("`!pokernight`", "This will generate a game.", false);
            help.addField("`!sketchyheads`", "This will generate a game.", false);
            help.addField("`!sketchyartists`", "This will generate a game.", false);
            help.addField("`!spellcaster`", "This will generate a game.", false);
            help.addField("`!spellcast`", "This will generate a game.", false);
            help.addField("`!wordsnacks`", "This will generate a game.", false);
            help.addField("`!youtube`", "This will generate a game.", false);
            event.getChannel().sendMessageEmbeds(help.build()).queue(message -> {
                message.delete().queueAfter(15, TimeUnit.MINUTES);
            });
        }
    }
}
