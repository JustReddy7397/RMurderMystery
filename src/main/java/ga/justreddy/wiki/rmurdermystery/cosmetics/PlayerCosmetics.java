package ga.justreddy.wiki.rmurdermystery.cosmetics;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.builder.MongoBuilder;

public class PlayerCosmetics {

    private int knifeSkinSelect;
    private int victoryDanceSelect;
    private int lastWordsSelect;

    private final GamePlayer gamePlayer;

    public PlayerCosmetics(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
        this.knifeSkinSelect = gamePlayer.getPlayerData().getKnifeSkin() != null ? gamePlayer.getPlayerData().getKnifeSkin() : 0;
        this.victoryDanceSelect = gamePlayer.getPlayerData().getVictoryDance() != null ? gamePlayer.getPlayerData().getVictoryDance() : 0;
        this.lastWordsSelect = gamePlayer.getPlayerData().getLastWords() != null ? gamePlayer.getPlayerData().getLastWords() : 0;
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
        gamePlayer.getPlayerData().setLastWords(lastWordsSelect);
    }
}
