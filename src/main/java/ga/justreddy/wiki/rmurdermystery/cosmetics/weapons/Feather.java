package ga.justreddy.wiki.rmurdermystery.cosmetics.weapons;

import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.cosmetics.KnifeSkins;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Feather extends KnifeSkins {
    public Feather() {
        super("Feather", 1, 300, "mm.cosmetics.weapons.feather");
    }

    @Override
    public void give(GamePlayer gamePlayer) {
        ItemStack itemStack = new ItemStack(Material.FEATHER);
        gamePlayer.setItem(0, itemStack);
    }
}