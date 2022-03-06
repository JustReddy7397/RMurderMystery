package ga.justreddy.wiki.rmurdermystery.commands.subcommands;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.player.PlayerController;
import ga.justreddy.wiki.rmurdermystery.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class JoinCommand extends Command {
    public JoinCommand() {
        super("join", "Join an arena", "/mm join <arena>", true, "");
    }

    @Override
    public void onCommand(MurderMystery plugin, Player player, String[] args) {
        String arenaName = args[1];
        GamePlayer gamePlayer = PlayerController.getPlayerController().get(player.getUniqueId());
        Arena arena = ArenaManager.getArenaManager().getArena(arenaName);
        ArenaManager.getArenaManager().joinArena(gamePlayer, arena);
    }

    @Override
    public void onCommand(MurderMystery plugin, CommandSender sender, String[] args) {

    }
}
