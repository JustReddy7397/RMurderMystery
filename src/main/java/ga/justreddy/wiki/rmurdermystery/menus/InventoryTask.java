package ga.justreddy.wiki.rmurdermystery.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InventoryTask implements Runnable {

    private final AbstractInventory inventory;

    InventoryTask(AbstractInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void run() {
        for (UUID uuid : inventory.getOpenInventories()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                inventory.refreshInventory(player, player.getOpenInventory().getTopInventory());
            }
        }
    }
}
