package ga.justreddy.wiki.rmurdermystery.action.actions;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.action.Action;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;

public class JoinAction implements Action {

    @Override
    public String getIdentifier() {
        return "JOIN_ARENA";
    }

    @Override
    public void execute(MurderMystery plugin, GamePlayer gamePlayer, String data) {
        ArenaManager.getArenaManager().joinArena(gamePlayer, ArenaManager.getArenaManager().getArena(data));
    }
}
