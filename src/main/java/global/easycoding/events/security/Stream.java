package global.easycoding.events.security;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Stream extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        if (event.getMember().getVoiceState().isStream()) {
            // send private message to user
            event.getMember().getUser().openPrivateChannel().queue(message -> {
                message.delete().queueAfter(15, TimeUnit.MINUTES);
            },channel -> {
                EmbedBuilder streaming = new EmbedBuilder();
                streaming.setTitle("You are streaming!");
                streaming.setDescription("You are streaming in " + event.getGuild().getName() + "!");
                streaming.addField("Please stay safe", "If you are streaming, please do not leak any private information", false);
                streaming.addField("This Message will be deleted in 15 minutes", "", false);
                streaming.setColor(Color.RED);
            });
        }
    }
}
