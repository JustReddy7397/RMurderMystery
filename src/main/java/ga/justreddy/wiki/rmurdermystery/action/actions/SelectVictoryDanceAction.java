package ga.justreddy.wiki.rmurdermystery.action.actions;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.action.Action;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.controller.VictoryDancesController;

public class SelectVictoryDanceAction implements Action {

    @Override
    public String getIdentifier() {
        return "SELECT_VICTORY_DANCE";
    }

    @Override
    public void execute(MurderMystery plugin, GamePlayer gamePlayer, String data) {
        if(!VictoryDancesController.getVictoryDancesController().getVictoryDances().containsValue(VictoryDancesController.getVictoryDancesController().getById(Integer.parseInt(data)))){
            gamePlayer.sendMessage("&cThere is no victorydance with that ID");
            return;
        }
        gamePlayer.sendMessage("&7Successfully selected the victorydance: &a" + VictoryDancesController.getVictoryDancesController().getById(Integer.parseInt(data)).getName());
        gamePlayer.getCosmetics().setKnifeSkinSelect(Integer.parseInt(data));
    }
}
