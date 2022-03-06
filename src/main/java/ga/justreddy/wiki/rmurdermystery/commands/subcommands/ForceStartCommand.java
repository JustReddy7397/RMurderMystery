package ga.justreddy.wiki.rmurdermystery.commands.subcommands;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.player.PlayerController;
import ga.justreddy.wiki.rmurdermystery.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceStartCommand extends Command {
    public ForceStartCommand() {
        super("forcestart", "Forcestart a game", "/mm forcestart", true, "");
    }

    @Override
    public void onCommand(MurderMystery plugin, Player player, String[] args) {
        GamePlayer gamePlayer = PlayerController.getPlayerController().get(player.getUniqueId());
        for(Arena arena : ArenaManager.getArenaManager().getArenas()){
            if(arena.getPlayers().contains(gamePlayer) && arena.getGameState() == GameState.WAITING){
                arena.getGameManager().setGameState(arena, GameState.PLAYING);
            }
        }
    }

    @Override
    public void onCommand(MurderMystery plugin, CommandSender sender, String[] args) {

    }
}
