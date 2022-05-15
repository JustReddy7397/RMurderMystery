package ga.justreddy.wiki.rmurdermystery.cosmetics.weapons;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.cosmetics.KnifeSkins;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Feather extends KnifeSkins {
    public Feather() {
        super("Feather", 1, 300, "mm.cosmetics.weapons.feather", XMaterial.FEATHER);
    }

    @Override
    public void give(GamePlayer gamePlayer) {
        gamePlayer.setItem(0, getMaterial().parseItem());
    }
}