package global.easycoding.cmd;

import global.easycoding.entity.Command;
import global.easycoding.events.CommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.lang.reflect.Member;

public class Prefix extends Command {
    protected Prefix(String name) {
        super(name);
    }

    @Override
    public void execute(CommandEvent event) {
        User user = event.getAuthor();
        Member member = event.getMember();
        String[] args = event.getArgs();
        Guild guild = event.getGuild()

    }
}
