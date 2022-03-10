package ga.justreddy.wiki.rmurdermystery.controller;

import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.cosmetics.LastWords;

import java.util.HashMap;
import java.util.Map;

public class LastWordsController {

    private static LastWordsController lastWordsController;
    private final Map<String, LastWords> lastWords = new HashMap<>();

    public static LastWordsController getLastWordsController() {
        if(lastWordsController == null) lastWordsController = new LastWordsController();
        return lastWordsController;
    }

    private LastWordsController(){}

    public LastWords getById(int id){
        LastWords c = null;
        for(LastWords cosmetic : lastWords.values()){
            if(cosmetic.getId() == id){
                c = cosmetic;
            }
        }
        return c;
    }

    public LastWords getByGamePlayer(GamePlayer gamePlayer){
        LastWords c = null;
        for(LastWords cosmetics : lastWords.values()){
            if(cosmetics.getId() == gamePlayer.getCosmetics().getVictoryDanceSelect()){
                c = cosmetics;
            }
        }
        return c;
    }

    public void shutDown(){
        lastWords.clear();
    }

    public Map<String, LastWords> getLastWords() {
        return lastWords;
    }

}
