package ga.justreddy.wiki.rmurdermystery.action.actions;

import com.cryptomorin.xseries.XSound;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.action.Action;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import org.bukkit.Bukkit;

public class PlaySoundAction implements Action {

    @Override
    public String getIdentifier() {
        return "PLAY_SOUND";
    }

    @Override
    public void execute(MurderMystery plugin, GamePlayer gamePlayer, String data) {
        try {
            gamePlayer.getPlayer().playSound(gamePlayer.getLocation(), XSound.matchXSound(data).get().parseSound(), 1L, 1L);
        } catch (Exception ex) {
            Bukkit.getLogger().warning("[RMurderMystery Action] Invalid sound name: " + data.toUpperCase());
        }
    }
}
