package ga.justreddy.wiki.rmurdermystery.cosmetics;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;

public abstract class KnifeSkins extends Cosmetics{

    private final XMaterial material;

    public KnifeSkins(String name, int id, int cost, String permission, XMaterial material) {
        super(name, id, cost, permission);
        this.material = material;
    }

    public XMaterial getMaterial() {
        return material;
    }

    public abstract void give(GamePlayer gamePlayer);

}
