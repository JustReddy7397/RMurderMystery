package ga.justreddy.wiki.rmurdermystery.action.actions;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.action.Action;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import org.bukkit.Bukkit;

public class ConsoleCommandAction implements Action {

    @Override
    public String getIdentifier() {
        return "CMD_CONSOLE";
    }

    @Override
    public void execute(MurderMystery plugin, GamePlayer gamePlayer, String data) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), data);
    }
}
