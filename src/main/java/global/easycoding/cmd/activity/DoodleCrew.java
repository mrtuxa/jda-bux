package global.easycoding.cmd.activity;

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

public class DoodleCrew extends ListenerAdapter {

    // load dotenv from Main
    private static final Dotenv dotenv = Main.dotenv;

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String game = dotenv.get("DOODLE_CREW");
        if (event.getMessage().getContentStripped().equals("!doodlecrew")) {
            if (event.getMember().getVoiceState().getChannel() == null) {
                event.getChannel().sendMessageEmbeds(new EmbedBuilder().setTitle("Activity Manager").setDescription("You must be in a voice channel to play Doodle Crew").setColor(Color.RED).build()).queue(message -> {
                    message.delete().queueAfter(15, TimeUnit.MINUTES);
                });
                return;
            }
            VoiceChannel vc = event.getGuild().getVoiceChannelById(event.getMember().getVoiceState().getChannel().getId());
            String invite = vc.createInvite().setTargetApplication(game)
                    .complete()
                    .getUrl();
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setTitle("Doodle Crew").setDescription("Doodle Crew").build()).setActionRow(Button.link(invite, "Doodle Crew")).queue(message -> {
                message.delete().queueAfter(15, TimeUnit.MINUTES);
            });
        }
    }
}
