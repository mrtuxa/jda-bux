package global.easycoding.cmd.unstable.forum;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.handle.ChannelCreateHandler;
import org.jetbrains.annotations.NotNull;

public class ForumChannel extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMessage().getContentStripped().equals("!forum")) {
            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {

            }
        }
    }
}
