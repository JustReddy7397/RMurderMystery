package ga.justreddy.wiki.rmurdermystery.cosmetics;

import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;

public abstract class VictoryDances extends Cosmetics {
    public VictoryDances(String name, int id, int cost, String permission) {
        super(name, id, cost, permission);
    }

    public abstract void start(GamePlayer gamePlayer);

    public abstract void stop(GamePlayer gamePlayer);

}
