
package global.easycoding.core;

import global.easycoding.cmd.Help;
import global.easycoding.cmd.SetTicket;
import global.easycoding.cmd.activity.stable.*;
import global.easycoding.database.DatabaseConnection;
import global.easycoding.events.secure.Streaming;
import global.easycoding.listener.CommandListener;
import global.easycoding.listener.GameListener;
import global.easycoding.util.GameUtil;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Main {
    static HashMap<Long, String> prefixes = new HashMap<>();
    public static Dotenv dotenv = Dotenv.load();
    private static final String guild = dotenv.get("GUILD_ID");
    private static ShardManager shardManager;
    private static final boolean enableDatabase = false;
    private static final DatabaseConnection.DBType dbType = DatabaseConnection.DBType.SQLite;

    private static boolean debug = true;
    private static DatabaseConnection database = null;

    public void Main() throws LoginException {
        String TOKEN = null;
        try {
            File env = Paths.get(".env").toFile();
            if (!env.exists()) {
                System.out.println("Please create a .env file with the example in the repository and paste all required informations");
                Scanner scanner = new Scanner(System.in);
                TOKEN = scanner.nextLine();
                System.out.println("[INFO] Creating .env - please wait");
                if (!env.createNewFile()) {
                    System.out.println(
                            "[ERROR] Could not create .env - please create ths file and paste in the example .env and fill in all required informations");
                    scanner.close();
                    return;
                }
                Files.write(env.toPath(), TOKEN.getBytes());
                scanner.close();
            }
            TOKEN = new String(Files.readAllBytes(env.toPath()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (TOKEN == null) return;
        if (enableDatabase) database = new DatabaseConnection(dbType);
        if (database != null) {
            database = null;
            System.out.println("[ERROR] Database connection failed. Continuing without database");
        } else {
            database.update("" +
                    "CREATE TABLE IF NOT EXISTS guildprefix (guildprefix VARCHAR(18) NOT NULL, prefix VARCHAR(8) NOT" +
                    "NULL)");
        }
        List<GatewayIntent> intents = new ArrayList<>(
                Arrays.asList(GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                        GatewayIntent.DIRECT_MESSAGE_TYPING,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.GUILD_BANS,
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                        GatewayIntent.GUILD_INVITES,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_MESSAGE_TYPING,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_WEBHOOKS,
                        GatewayIntent.MESSAGE_CONTENT)
        );
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(TOKEN);
        builder.setActivity(Activity.listening("Spotify"));
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.enableIntents(intents);
        builder.enableCache(CacheFlag.VOICE_STATE);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        shardManager = builder.build();
        shardManager.addEventListener(new AskAway());
        shardManager.addEventListener(new AwkWord());
        shardManager.addEventListener(new Betrayalio());
        shardManager.addEventListener(new BlazingEight());
        shardManager.addEventListener(new BobbleLeague());
        shardManager.addEventListener(new CheckersInThePark());
        shardManager.addEventListener(new ChessInThePark());
        shardManager.addEventListener(new DoodleCrew());
        shardManager.addEventListener(new DoodleCrew());
        shardManager.addEventListener(new FisingtonIo());
        shardManager.addEventListener(new Help());
        shardManager.addEventListener(new Landio());
        shardManager.addEventListener(new LetterLeague());
        shardManager.addEventListener(new PokerNight());
        shardManager.addEventListener(new PuttParty());
        shardManager.addEventListener(new SketchHeads());
        shardManager.addEventListener(new SketchyArtist());
        shardManager.addEventListener(new SpellCast());
        shardManager.addEventListener(new YouTube());
        shardManager.addEventListener(new WordSnacks());
        shardManager.addEventListener(new SetTicket());
        shardManager.addEventListener(new Streaming());
        shardManager.addEventListener(new GameListener());
        shardManager.addEventListener(new CommandListener());
        GameUtil.runGameTimer();

    }


    /*
    * Debug information for developer
    * > Limit update to 10 seconds minimum because of JDA shard checks
     */

    private static long lastDebugInfoUpdate = -1L;
    private static String debugInfo = "";

    private static void updateDebugInfo() {
        long now = System.currentTimeMillis();
        if (now - lastDebugInfoUpdate < 1000) return;
        lastDebugInfoUpdate = now;
        int a = enableDatabase ? 1 : 0;
        int b = enableDatabase ? database.isConnceted() ? 1 : 0 : 0;
        int c = 0;
        int d = shardManager.getShardsTotal();
        for (JDA shard : shardManager.getShards()) if (shard.getStatus() == JDA.Status.CONNECTED) c++;
        debugInfo = a + b +c +d + "";
    }

    /*
    Print a message when debug is on
    */

    public static void debug(String log) {
        if (debug) {
            updateDebugInfo();
            System.out.println("[DEBUG] " + debugInfo + log);
        }
    }

    public static void removePrefix(long guildId, Guild guild) {
        prefixes.remove(guildId);
        if (database != null) {
            database.update("DELETE FROM guildPrefix WHERE guildId=?;", guild.getIdLong());
        }
    }

    public static void setPrefix(Guild guild, String prefix) {
        prefixes.put(guild.getIdLong(), prefix);
        if (database != null) {
            database.update("DELETE FROM guildPrefix WHERE guildId=?;", guild.getIdLong());
            database.update("INSERT INTO guildPrefix VALUES (?, ?);", guild.getIdLong(), prefix);
        }
    }

    public static String getPrefix(Guild guild) {
        if (prefixes.containsKey(guild.getIdLong())) return prefixes.get(guild.getId());
        if (database != null) {
            try (ResultSet resultSet = database.query("SELECT prefix FROM guildPrefix WHERE guildId=?;", guild.getIdLong())) {
                if (resultSet.next()) {
                    String prefix = resultSet.getString("prefix");
                    prefixes.put(guild.getIdLong(), prefix);
                    return prefix;
                }
                prefixes.put(guild.getIdLong(), "!");
            } catch (SQLException e) {
                System.out.println("[Error] retrieving guild prefix of guild  id" + guild.getId() + ": " + e.getMessage());
            }
        }
        return "!";
    }

        public static void main(String[] args) throws LoginException {
            Main api = new Main();
        }

        public static ShardManager getShardManager() {
            return shardManager;
        }
    }
