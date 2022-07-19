package global.easycoding.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MemberJoin extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        User user = event.getMember().getUser();

        EmbedBuilder join = new EmbedBuilder();
        join.setTitle("easycoding.global");
        join.setDescription("Welcome to easycoding.global, " + user.getName() + "!");
        join.setColor(0x00ff00);
        join.addField("\uD83D\uDCCD IMPORTANT INFORMATION", "Please read the rules and guidelines in <#999000326709461113>", false);


        event.getGuild().getTextChannelById("999000326709461113").sendMessageEmbeds(join.build()).mention(user).queue();
    }
}
