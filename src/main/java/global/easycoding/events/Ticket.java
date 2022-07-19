package global.easycoding.events;

import com.github.kennedyoliveira.pastebin4j.*;
import global.easycoding.core.Main;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class Ticket extends ListenerAdapter {

    private static Dotenv dotenv = Main.dotenv;

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        event.deferEdit().queue();

        if(event.getButton().equals("openTicket")) {
            String role = String.valueOf(event.getMember().getRoles());
            if (!role.contains("Ticket Mute")) {
                Guild guild = event.getGuild();
                String nickname = event.getMember().getEffectiveName();

                EmbedBuilder ticket = new EmbedBuilder();
                ticket.setTitle("Ticket System");
                ticket.setDescription("This is the ticket System for the server.");
                ticket.setColor(0x00ff00);
                ticket.addField("Please be patient", "someone will be with you shortly. Advice, it will not help you faster if you write to someone. For questions that are not server relevant please ask in the public text channels.", false);
                ticket.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
                ticket.setFooter("EasyCoding", event.getJDA().getSelfUser().getAvatarUrl());
                event.getChannel().sendMessageEmbeds(ticket.build()).setActionRow(Button.primary("openTicket", "\uD83D\uDCE9 Open Ticket")).queue();

                int min = 1000;
                int max = 9999;
                int random = (int) (Math.random() * (max - min + 1)) + min;

                event.reply("Your ticket number is: " + random).setEphemeral(true).queue();

                guild.createTextChannel("ticket-" + nickname + "-" + random, guild.getCategoryById("997617070592245822")).setTopic("Ticket for " + nickname)
                        .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), null)
                        .addPermissionOverride(guild.getRoleById("997618897060642846"), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), null)
                        .complete().sendMessageEmbeds(ticket.build()).setActionRow(Button.danger("closeButton", "\uD83D\uDCE9 Close Ticket"), Button.primary("solved", "Problem already solved")).queue();

                EmbedBuilder team = new EmbedBuilder();
                team.setTitle("Ticket System");
                team.setDescription("This is the ticket System for the server.");
                team.setColor(0x00ff00);
                team.addField("New Ticket", "A new ticket has been created by " + nickname + " with the number " + random, false);

                team.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
                team.setFooter("EasyCoding", event.getJDA().getSelfUser().getAvatarUrl());
                guild.getTextChannelById("996839235837513878").sendMessageEmbeds(team.build()).queue();
            } else {
                event.reply("You are muted and cannot use this command.").setEphemeral(true).queue();
            }
        } else if (event.getButton().equals("closeButton")) {
            String role = String.valueOf(event.getMember().getRoles());

            if (role.contains("Tickets")) {
                event.getInteraction().getChannel().delete().queue();
            } else {
                event.reply("You are not allowed to use this function.").setEphemeral(true).queue();
            }

            final String devKey = dotenv.get("DEV_KEY");
            final String username = dotenv.get("USERNAME");
            final String password = dotenv.get("PASSWORD");

            final PasteBin pasteBin = new PasteBin(new AccountCredentials(devKey, username, password));

            final Paste paste = new Paste();
            paste.setTitle(event.getChannel().getName());
            paste.setExpiration(PasteExpiration.NEVER);
            paste.setHighLight(PasteHighLight.TEXT);
            paste.setContent(event.getChannel().getHistory().retrievePast(100).complete().toString());
            final String url = pasteBin.createPaste(paste);

            EmbedBuilder ticket = new EmbedBuilder();
            ticket.setTitle("Ticket System");
            ticket.setDescription("Ticket Log" + url);
            ticket.setColor(0x00ff00);
            ticket.addField("Ticket Closed", "The ticket has been closed by " + event.getMember().getEffectiveName() + ".", false);
            ticket.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
            ticket.setFooter("EasyCoding", event.getJDA().getSelfUser().getAvatarUrl());

            event.getGuild().getTextChannelById("996839235837513878").sendMessageEmbeds(ticket.build()).queue();

        } else if (event.getButton().equals("solved")) {
            event.getInteraction().getChannel().delete().queue();
            event.reply("The ticket has been solved.").setEphemeral(true).queue();
            EmbedBuilder solved = new EmbedBuilder();
            solved.setTitle("Ticket System");
            solved.setDescription("This is the ticket System for the server.");
            solved.setColor(0x00ff00);
            solved.addField("Ticket solved", "The ticket has been solved by " + event.getMember().getEffectiveName(), false);
            solved.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
            solved.setFooter("EasyCoding", event.getJDA().getSelfUser().getAvatarUrl());
            event.getGuild().getTextChannelById("996839235837513878").sendMessageEmbeds(solved.build()).queue();
        }
    }
}
