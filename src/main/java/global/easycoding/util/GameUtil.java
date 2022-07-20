package global.easycoding.util;

import global.easycoding.games.Game;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class GameUtil {
   private static final HashMap<Long, Game> games = new HashMap<>();

   public static void setGame(long userID, Game game) {
       games.put(userID, game);
   }

   public static boolean hasGame(long userID) {
       return games.containsKey(userID);
   }

   public static Game getGame(long userID) {
       return games.get(userID);
   }

    public static void removeGame(long userID) {
         games.remove(userID);
    }

    public static void sendGameEmbed(MessageChannel channel, String level, String s, User user) {
        EmbedBuilder gameEmbed = new EmbedBuilder();
        gameEmbed.setTitle("easycoding.global | Level " + level);
        gameEmbed.setDescription("Game started by " + user.getName() + "!");
        gameEmbed.addField("Enter direction (`up`, `down`, `left`, `right`/`wasd`)", "`r` to reset or `mr` to recreate the map", false);
        gameEmbed.addField("Player", user.getAsMention(), false);
        gameEmbed.setColor(0x00ff00);
        gameEmbed.setFooter("EasyCoding", user.getAvatarUrl());
        channel.sendMessageEmbeds(gameEmbed.build()).queue(message -> {
            message.addReaction(Emoji.fromUnicode("⬅")).queue();
            message.addReaction(Emoji.fromUnicode("➡")).queue();
            message.addReaction(Emoji.fromUnicode("⬆")).queue();
            message.addReaction(Emoji.fromUnicode("⬇")).queue();
            message.addReaction(Emoji.fromUnicode("🔄")).queue();
            Game theGame = GameUtil.getGame(user.getIdLong());
            theGame.setGameMessage(message);
        });
    }

    public static void updateGameEmbed(Message message, String level, String game, User user) {
        EmbedBuilder gameEmbed = new EmbedBuilder();
        gameEmbed.setTitle("easycoding.global | Level " + level);
        gameEmbed.setDescription("Game started by " + user.getName() + "!");
        gameEmbed.addField("Enter direction (`up`, `down`, `left`, `right`/`wasd`)", "`r` to reset or `mr` to recreate the map", false);
        gameEmbed.addField("Player", user.getAsMention(), false);
        gameEmbed.setColor(0x00ff00);
        gameEmbed.setFooter("EasyCoding", user.getAvatarUrl());
        message.editMessageEmbeds(gameEmbed.build()).queue();
    }

    public static void sendWinEmbed(Guild guild, Message message, String level) {
       long description = Long.parseLong("Congratulations! You have completed level " + level + "!" + "\n Type `!continue` to continue to Level " + level + "`!stop` to quit");
       EmbedBuilder gameEmbed = new EmbedBuilder();
       gameEmbed.setTitle("easycoding.global | You win!");
       gameEmbed.setDescription(String.valueOf(description));
       gameEmbed.setFooter("You can also press any reaction to continue\n EasyCoding",  "\n" + guild.getIconUrl());
       message.editMessageEmbeds(gameEmbed.build()).queue();
    }

    public static void runGameTimer() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                for (long playerId : games.keySet()) {
                    Game game = games.get(playerId);
                    long timeDifference = now - game.lastAction;
                    if (timeDifference > 10 * 60 * 1000) {
                        System.out.println("[INFO] Stopped inactive game of " + playerId);
                        game.stop();
                        GameUtil.removeGame(playerId);
                    }
                }
            }
        }, 10 * 60 * 1000, 60 * 1000);
    }

}
