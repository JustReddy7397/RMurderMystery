package ga.justreddy.wiki.rmurdermystery.action.actions;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.action.Action;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;

public class CloseInventoryAction implements Action {
    @Override
    public String getIdentifier() {
        return "CLOSE";
    }

    @Override
    public void execute(MurderMystery plugin, GamePlayer gamePlayer, String data) {
        gamePlayer.getPlayer().closeInventory();
    }
}
