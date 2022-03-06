package ga.justreddy.wiki.rmurdermystery.action.actions;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.action.Action;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.menus.AbstractInventory;

public class MenuAction implements Action {

    @Override
    public String getIdentifier() {
        return "MENU";
    }

    @Override
    public void execute(MurderMystery plugin, GamePlayer gamePlayer, String data) {
        AbstractInventory inventory = plugin.getMenuManager().getInventory(data);

        if (inventory != null) {
            inventory.openInventory(gamePlayer.getPlayer());
        } else {
            plugin.getLogger().warning("[MENU] Action Failed: Menu '" + data + "' not found.");
        }
    }
}
