package global.easycoding.events.activity;

import global.easycoding.core.Main;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Betrayalio extends ListenerAdapter {
    private static Dotenv dotenv = Main.dotenv;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String game = dotenv.get("BETRAYAL_IO");
        if (event.getMessage().getContentStripped().equals("!betrayal")) {
            if (event.getMember().getVoiceState().getChannel() == null) {
                event.getChannel().sendMessageEmbeds(new EmbedBuilder().setTitle("Activity Manager").setDescription("You must be in a voice channel to play Betrayal.io").setColor(Color.RED).build()).queue(message -> {
                    message.delete().queueAfter(15, TimeUnit.MINUTES);
                });
                return;
            }
            VoiceChannel vc = event.getGuild().getVoiceChannelById(event.getMember().getVoiceState().getChannel().getId());
            String invite = vc.createInvite().setTargetApplication(game)
                    .complete()
                    .getUrl();
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setTitle("Betrayal.io").setDescription("Play Betrayal.io with your friends").build()).setActionRow(Button.link(invite, "Betrayal.io")).queue(message -> {
                message.delete().queueAfter(15, TimeUnit.MINUTES);
            });
        }
    }
}
