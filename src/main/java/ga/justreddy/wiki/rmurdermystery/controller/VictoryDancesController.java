package ga.justreddy.wiki.rmurdermystery.controller;

import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.cosmetics.VictoryDances;

import java.util.HashMap;
import java.util.Map;

public class VictoryDancesController {

    private static VictoryDancesController victoryDancesController;
    private final Map<String, VictoryDances> victoryDances = new HashMap<>();

    public static VictoryDancesController getVictoryDancesController() {
        if(victoryDancesController == null) victoryDancesController = new VictoryDancesController();
        return victoryDancesController;
    }

    private VictoryDancesController(){}

    public VictoryDances getById(int id){
        VictoryDances c = null;
        for(VictoryDances cosmetic : victoryDances.values()){
            if(cosmetic.getId() == id){
                c = cosmetic;
            }
        }
        return c;
    }

    public VictoryDances getByGamePlayer(GamePlayer gamePlayer){
        VictoryDances c = null;
        for(VictoryDances cosmetics : victoryDances.values()){
            if(cosmetics.getId() == gamePlayer.getCosmetics().getVictoryDanceSelect()){
                c = cosmetics;
            }
        }
        return c;
    }

    public void shutDown(){
        victoryDances.clear();
    }

    public Map<String, VictoryDances> getVictoryDances() {
        return victoryDances;
    }

}
