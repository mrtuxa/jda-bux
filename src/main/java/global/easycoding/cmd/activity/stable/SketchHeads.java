package global.easycoding.cmd.activity.stable;

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

public class SketchHeads extends ListenerAdapter {

    // load dotenv from Main
    private static final Dotenv dotenv = Main.dotenv;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String game = dotenv.get("SKETCH_HEADS");
        if (event.getMessage().getContentStripped().equals("!sketchheads")) {
            if (event.getMember().getVoiceState().getChannel() == null) {
                event.getChannel().sendMessageEmbeds(new EmbedBuilder().setTitle("Activity Manager").setDescription("You must be in a voice channel to play Sketch Heads").setColor(Color.RED).build()).queue(message -> {
                    message.delete().queueAfter(15, TimeUnit.MINUTES);
                });
                return;
            }
            VoiceChannel vc = event.getGuild().getVoiceChannelById(event.getMember().getVoiceState().getChannel().getId());
            String invite = vc.createInvite().setTargetApplication(game)
                    .complete()
                    .getUrl();
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setTitle("Sketch Heads").setDescription("Sketch Heads").build()).setActionRow(Button.link(invite, "Sketch Heads")).queue(message -> {
                message.delete().queueAfter(15, TimeUnit.MINUTES);
            });
        }
    }
}
