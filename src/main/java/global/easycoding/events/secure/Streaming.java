package global.easycoding.events.secure;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceStreamEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class Streaming extends ListenerAdapter {

    @Override
    public void onGuildVoiceStream(@NotNull GuildVoiceStreamEvent event) {
        if (event.getMember().getVoiceState().isStream()) {
            EmbedBuilder streaming = new EmbedBuilder();
            streaming.setTitle("easycoding.global");
            streaming.setDescription("This is the Security System for the server.");
            streaming.addField("\uD83D\uDCCD IMPORTANT INFORMATION", "Please do not leak any informations about you or any other peson", false);
            streaming.setColor(0x00ff00);
            streaming.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
            streaming.setFooter("EasyCoding", event.getJDA().getSelfUser().getAvatarUrl());

            // send private message to user
            event.getMember().getUser().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessageEmbeds(streaming.build()).queue(message -> {
                    message.delete().queueAfter(120, TimeUnit.SECONDS);
                });
            });
        }
    }
}
