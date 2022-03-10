package ga.justreddy.wiki.rmurdermystery.action.actions;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.action.Action;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.controller.KnifeSkinsController;

public class SelectKnifeSkinAction implements Action {

    @Override
    public String getIdentifier() {
        return "SELECT_KNIFE_SKIN";
    }

    @Override
    public void execute(MurderMystery plugin, GamePlayer gamePlayer, String data) {
        if(!KnifeSkinsController.getKnifeSkinsController().getKnifeskins().containsValue(KnifeSkinsController.getKnifeSkinsController().getById(Integer.parseInt(data)))){
            gamePlayer.sendMessage("&cThere is no knife skin with that ID");
            return;
        }
        gamePlayer.getCosmetics().setKnifeSkinSelect(Integer.parseInt(data));
        gamePlayer.sendMessage("&7Successfully selected the knifeskin: &a" + KnifeSkinsController.getKnifeSkinsController().getById(Integer.parseInt(data)).getName());
    }
}
