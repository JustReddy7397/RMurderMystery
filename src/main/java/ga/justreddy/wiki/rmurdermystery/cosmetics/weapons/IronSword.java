package ga.justreddy.wiki.rmurdermystery.cosmetics.weapons;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.cosmetics.KnifeSkins;
import org.bukkit.inventory.ItemStack;

public class IronSword extends KnifeSkins {

    public IronSword() {
        super("Iron Sword", 0, 0, "mm.cosmetics.weapons.ironsword", XMaterial.IRON_SWORD);
    }

    @Override
    public void give(GamePlayer gamePlayer) {
        gamePlayer.setItem(0, getMaterial().parseItem());
    }
}
