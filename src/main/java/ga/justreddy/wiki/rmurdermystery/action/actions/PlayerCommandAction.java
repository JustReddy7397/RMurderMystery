package ga.justreddy.wiki.rmurdermystery.action.actions;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.action.Action;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;

public class PlayerCommandAction implements Action {
    @Override
    public String getIdentifier() {
        return "CMD_PLAYER";
    }

    @Override
    public void execute(MurderMystery plugin, GamePlayer gamePlayer, String data) {
        gamePlayer.getPlayer().chat(data.contains("/") ? data : "/" + data);
    }
}
