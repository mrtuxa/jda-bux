package global.easycoding.games;

import global.easycoding.core.Main;
import global.easycoding.objects.Grid;
import net.dv8tion.jda.api.entities.*;
import global.easycoding.util.GameUtil;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Game {
    long gameMessageId;

    long channelID;
    User user;
    String playerEmote = ":flushed:";
    public boolean gameActiv = false;
    public int level =1;
    int width = 9;
    int height = 6;
    public long lastAction;
    Grid grid = new Grid(width, height, level, playerEmote);

    public Game(User user) {
        this.user = user;
    }

    public void setPlayerEmote(String emote) {
        playerEmote = emote;
    }

    public void setGameMessage(Message gmessage) {
        gameMessageId = gmessage.getIdLong();
        channelID = gmessage.getChannel().getIdLong();
    }

    public void newGame(MessageChannel channel) {
        if (!gameActiv) {
            width = 9;
            height = 6;
            for (int i = 1; i < level; i++) updateWidthHeight();
            grid = new Grid(width, height, level, playerEmote);
            gameActiv = true;
            lastAction = System.currentTimeMillis();

            GameUtil.sendGameEmbed(channel, String.valueOf(level), grid.toString(), user);
        }
    }

    // This method used to something earlier. (I actually just forgot what I used it for)
    // It did not work like it was supposed to, so it was changed to this basic line.
    private void queue(RestAction<Message> restAction, Consumer<? super Message> success) {
        restAction.queue(success);
    }

    public void stop() {
        gameActiv = false;
        TextChannel textChannel = Main.getShardManager().getTextChannelById(channelID);
        if (textChannel != null) {
            textChannel.retrieveMessageById(gameMessageId).queue(gameMessage -> gameMessage.delete().queue());
        }
    }

    public void run(Guild guild, TextChannel channel, String userInput) {
        if (userInput.equals("stop") && gameActiv) {
            stop();
            channel.sendMessage("Thanks for playing, " + user.getAsMention() + "!")
                    .queue(msg -> msg.delete().queueAfter(10, TimeUnit.SECONDS));
        }
        if (userInput.equals("play") && !gameActiv) {
            newGame(channel);
        } else if (gameActiv) {
            lastAction = System.currentTimeMillis();
            boolean won = grid.hasWon();
            if (!won) {
                boolean moved = false;
                switch (userInput) {
                    case "up":
                    case "w":
                        moved = grid.getPlayer().moveUp();
                        break;
                    case "down":
                    case "s":
                        moved = grid.getPlayer().moveDown();
                        break;
                    case "left":
                    case "a":
                        moved = grid.getPlayer().moveLeft();
                        break;
                    case "right":
                    case "d":
                        moved = grid.getPlayer().moveRight();
                        break;
                    case "mr":
                        grid.resetMap();
                        moved = true;
                        break;
                    case "r":
                        grid.reset();
                        moved = true;
                        break;
                }
                grid.updateGrid();
                won = grid.hasWon();
                if (!won && moved) {
                    TextChannel textChannel = Main.getShardManager().getTextChannelById(channelID);
                    if (textChannel != null) {
                        queue(textChannel.retrieveMessageById(gameMessageId), gameMessage -> GameUtil.updateGameEmbed(gameMessage, String.valueOf(level), grid.toString(), user));
                    }
                }
            }
            if (won) {
                level += 1;
                updateWidthHeight();
                TextChannel textChannel = Main.getShardManager().getTextChannelById(channelID);
                if (textChannel != null) {
                    queue(
                            textChannel.retrieveMessageById(gameMessageId),
                            gameMessage -> GameUtil.sendWinEmbed(guild, gameMessage, String.valueOf(level)));
                }
                grid = new Grid(width, height, level, playerEmote);
            }
        }
    }

    private void updateWidthHeight() {
        if (width <13) {
            width += 2;
        }
        if (height < 8) {
            height += 1;
        }
    }
}