package ga.justreddy.wiki.rmurdermystery.commands.subcommands;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.player.PlayerController;
import ga.justreddy.wiki.rmurdermystery.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand extends Command {
    public LeaveCommand() {
        super("leave", "Leave the game", "/mm leave", true, "");
    }

    @Override
    public void onCommand(MurderMystery plugin, Player player, String[] args) {

        ArenaManager.getArenaManager().leaveArena(PlayerController.getPlayerController().get(player.getUniqueId()));

    }

    @Override
    public void onCommand(MurderMystery plugin, CommandSender sender, String[] args) {

    }
}
