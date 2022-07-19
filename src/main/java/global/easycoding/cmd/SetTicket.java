package global.easycoding.cmd;

import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class SetTicket extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentStripped().equals("setTicket")) {
            String role = String.valueOf(event.getMember().getRoles());
            if (role.contains("Owner")) {
                EmbedBuilder ticket = new EmbedBuilder();
                ticket.setTitle("Ticket System");
                ticket.setDescription("This is the ticket System for the server.");
                ticket.setColor(0x00ff00);
                ticket.addField("How to use", "Click on the button to open a ticket", false);
                ticket.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
                ticket.setFooter("EasyCoding", event.getJDA().getSelfUser().getAvatarUrl());
                event.getChannel().sendMessageEmbeds(ticket.build()).setActionRow(Button.primary("openTicket", "\uD83D\uDCE9 Open Ticket")).queue();
            }
        }
    }
}
