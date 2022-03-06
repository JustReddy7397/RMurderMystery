package ga.justreddy.wiki.rmurdermystery.action;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;

public interface Action {

    String getIdentifier();

    void execute(MurderMystery plugin, GamePlayer gamePlayer, String data);

}
