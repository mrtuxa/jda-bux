package global.easycoding.listener;

import global.easycoding.cmd.Help;
import global.easycoding.entity.Command;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandListener extends ListenerAdapter {
    private static final ArrayList<String> commandsNoPrefix = new ArrayList<>(
            Arrays.asList("w", "a", "s", "d", "up", "left", "down", "right", "r", "mr"));
    private static final HashMap<String, Command> commands = new HashMap<>();

    public CommandListener() {
        List<Command> Commands = new ArrayList<>(Arrays.asList(new Help(), ne))
    }
}
