package ga.justreddy.wiki.rmurdermystery.controller;

import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.cosmetics.KnifeSkins;
import ga.justreddy.wiki.rmurdermystery.cosmetics.VictoryDances;

import java.util.HashMap;
import java.util.Map;

public class KnifeSkinsController {

    private static KnifeSkinsController knifeSkinsController;
    private final Map<String, KnifeSkins> knifeskins = new HashMap<>();

    public static KnifeSkinsController getKnifeSkinsController() {
        if(knifeSkinsController == null) knifeSkinsController = new KnifeSkinsController();
        return knifeSkinsController;
    }

    private KnifeSkinsController(){}

    public KnifeSkins getById(int id){
        KnifeSkins c = null;
        for(KnifeSkins cosmetic : knifeskins.values()){
            if(cosmetic.getId() == id){
                c = cosmetic;
            }
        }
        return c;
    }

    public KnifeSkins getByGamePlayer(GamePlayer gamePlayer){
        KnifeSkins c = null;
        for(KnifeSkins cosmetics : knifeskins.values()){
            if(cosmetics.getId() == gamePlayer.getCosmetics().getVictoryDanceSelect()){
                c = cosmetics;
            }
        }
        return c;
    }

    public void shutDown(){
        knifeskins.clear();
    }

    public Map<String, KnifeSkins> getKnifeskins() {
        return knifeskins;
    }
}
