package ga.justreddy.wiki.rmurdermystery.cosmetics;

import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;

public abstract class KnifeSkins extends Cosmetics{

    public KnifeSkins(String name, int id, int cost, String permission) {
        super(name, id, cost, permission);
    }

    public abstract void give(GamePlayer gamePlayer);

}
