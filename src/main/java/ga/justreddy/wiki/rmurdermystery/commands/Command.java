package ga.justreddy.wiki.rmurdermystery.commands;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {

    private final String name;
    private final String description;
    private final String syntax;
    private final boolean playersOnly;
    private final String permission;
    private final List<String> aliases = new ArrayList<>();

    public Command(String name, String description, String syntax, boolean playersOnly, String permission, String... aliases){
        this.name = name;
        this.description = description;
        this.syntax = syntax;
        this.playersOnly = playersOnly;
        this.permission = permission;
        if(aliases != null){
            this.aliases.addAll(Arrays.asList(aliases));
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSyntax() {
        return syntax;
    }

    public boolean isPlayersOnly() {
        return playersOnly;
    }

    public String getPermission() {
        return permission;
    }


    public List<String> getAliases() {
        return aliases;
    }

    public abstract void onCommand(MurderMystery plugin, Player player, String[] args);

    public abstract void onCommand(MurderMystery plugin, CommandSender sender, String[] args);


}
