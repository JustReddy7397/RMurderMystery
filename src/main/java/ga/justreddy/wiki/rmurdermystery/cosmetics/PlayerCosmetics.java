package ga.justreddy.wiki.rmurdermystery.cosmetics;

import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayerCosmetics {

    private int knifeSkinSelect;
    private int victoryDanceSelect;

    private final GamePlayer gamePlayer;

    public PlayerCosmetics(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
        this.knifeSkinSelect = 0;
        this.victoryDanceSelect = 0;
    }

    public int getKnifeSkinSelect() {
        return knifeSkinSelect;
    }

    public void setKnifeSkinSelect(int knifeSkinSelect) {
        this.knifeSkinSelect = knifeSkinSelect;
        gamePlayer.getPlayerData().setKnifeSkin(knifeSkinSelect);
    }


    public void setVictoryDanceSelect(int victoryDanceSelect) {
        this.victoryDanceSelect = victoryDanceSelect;
        gamePlayer.getPlayerData().setVictoryDance(victoryDanceSelect);
    }

    public int getVictoryDanceSelect() {
        return victoryDanceSelect;
    }

}
