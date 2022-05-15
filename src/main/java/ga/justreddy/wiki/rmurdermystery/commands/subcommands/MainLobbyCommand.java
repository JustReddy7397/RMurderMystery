package ga.justreddy.wiki.rmurdermystery.commands.subcommands;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.commands.Command;
import ga.justreddy.wiki.rmurdermystery.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainLobbyCommand extends Command {
    public MainLobbyCommand() {
        super("setmainlobby", "Sets the main lobby", "/mm setmainlobby", true, "mm.setmainlobby");
    }

    @Override
    public void onCommand(MurderMystery plugin, Player player, String[] args) {
        plugin.getConfig().set("mainLobby", player.getLocation());
        plugin.saveConfig();
        player.sendMessage(Utils.format("&aSuccessfully set the main lobby"));
    }

    @Override
    public void onCommand(MurderMystery plugin, CommandSender sender, String[] args) {

    }
}
