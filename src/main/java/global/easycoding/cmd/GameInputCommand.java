package global.easycoding.cmd;

import com.vdurmont.emoji.EmojiManager;
import global.easycoding.entity.Command;
import global.easycoding.events.CommandEvent;
import global.easycoding.games.Game;
import global.easycoding.util.GameUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class GameInputCommand extends Command {

    protected GameInputCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandEvent event) {
        User user = event.getAuthor();
        String[] args = event.getArgs();
        String prefix = "!";
        Game game;
        if (!GameUtil.hasGame(user.getIdLong())) {
            game = new Game(user);
            GameUtil.setGame(user.getIdLong(), game);
        } else game = GameUtil.getGame(user.getIdLong());
        //
        String userInput = this.getName().toLowerCase();
        if (userInput.equals("play")) {
            if (!game.gameActiv) {
                if (args.length > 0 && EmojiManager.isEmoji(args[0])) game.setPlayerEmote(args[0]);
            } else {
                event.reply(user.getAsMention() + ", you already have an active game.\n Use `" + prefix + "stop` to stop your current game first");
            }
        }
        Guild guild = event.getGuild();
        TextChannel  channel = event.getTextChannel();
        game.run(event.getGuild(), channel, userInput);
        if (user.equals("stop")) GameUtil.removeGame(user.getIdLong());
        if (game.gameActiv && guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_MANAGE))
            event.getMessage().delete().queue();
    }
}
