package ga.justreddy.wiki.rmurdermystery.cosmetics;

import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;

public class PlayerCosmetics {

    private int knifeSkinSelect;
    private int victoryDanceSelect;
    private int lastWordsSelect;

    private final GamePlayer gamePlayer;

    public PlayerCosmetics(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
        this.knifeSkinSelect = 0;
        this.victoryDanceSelect = 0;
        this.lastWordsSelect = 0;
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

    public int getLastWordsSelect() {
        return lastWordsSelect;
    }

    public void setLastWordsSelect(int lastWordsSelect) {
        this.lastWordsSelect = lastWordsSelect;
    }
}
