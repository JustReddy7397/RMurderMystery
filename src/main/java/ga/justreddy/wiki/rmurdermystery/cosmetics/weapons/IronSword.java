package ga.justreddy.wiki.rmurdermystery.cosmetics.weapons;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.cosmetics.KnifeSkins;
import org.bukkit.inventory.ItemStack;

public class IronSword extends KnifeSkins {

    public IronSword() {
        super("Iron Sword", 0, 0, "mm.cosmetics.weapons.ironsword");
    }

    @Override
    public void give(GamePlayer gamePlayer) {
        final ItemStack itemStack = XMaterial.IRON_SWORD.parseItem();
        gamePlayer.setItem(0, itemStack);
    }
}
