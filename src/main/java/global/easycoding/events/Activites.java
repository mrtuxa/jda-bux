package global.easycoding.events;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class Activites extends ListenerAdapter {

    public static Dotenv dotenv = Dotenv.load();

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String game = dotenv.get("YOUTUBE");
        if (event.getMessage().getContentStripped().equals("!youtube")) {
            VoiceChannel vc = event.getGuild().getVoiceChannelById(event.getMember().getVoiceState().getChannel().getId());
            String invite = vc.createInvite().setTargetApplication(game)
                    .complete()
                    .getUrl();
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setTitle("Watch Together").setDescription("YouTube Together").build()).setActionRow(Button.link(invite, "Youtube")).queue( );
        } else if (event.getMessage().getContentStripped().equals("!print")) {
            VoiceChannel vc = event.getGuild().getVoiceChannelById(event.getMember().getVoiceState().getId().toString());
            System.out.println(vc);
        }
    }
}
