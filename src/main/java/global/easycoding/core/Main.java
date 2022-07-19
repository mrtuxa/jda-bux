package global.easycoding.core;

import global.easycoding.events.activity.*;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Main {

    public static Dotenv dotenv = Dotenv.load();
    private final ShardManager shardManager;

    private static final String guild = dotenv.get("GUILD_ID");
    public Main() throws LoginException {
        String token = dotenv.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setActivity(Activity.listening("Spotify"));
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.enableIntents(GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_WEBHOOKS, GatewayIntent.MESSAGE_CONTENT);
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
    }

    public ShardManager getShardManager() {
        return shardManager;
    }



    public static void main(String[] args) throws LoginException {
        try {
            Main api = new Main();
        } catch (LoginException e) {
            System.out.println("Token is invalid");
        }
    }
}
