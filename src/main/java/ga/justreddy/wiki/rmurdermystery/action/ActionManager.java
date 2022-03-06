package ga.justreddy.wiki.rmurdermystery.action;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.action.actions.*;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.player.PlayerController;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionManager {

    private final MurderMystery plugin;
    private final Map<String, Action> actions;

    public ActionManager(MurderMystery plugin){
        this.plugin = plugin;
        actions = new HashMap<>();
        load();
    }

    private void load(){
        registerAction(
                new CloseInventoryAction(),
                new ConsoleCommandAction(),
                new PlayerCommandAction(),
                new SelectKnifeSkinAction(),
                new PlaySoundAction(),
                new MenuAction(),
                new JoinAction(),
                new SelectVictoryDanceAction()
        );
    }

    public void registerAction(Action... actions) {
        Arrays.asList(actions).forEach(action -> this.actions.put(action.getIdentifier(), action));
    }

    public void executeActions(Player player, List<String> items) {
        items.forEach(item -> {
            String actionName = StringUtils.substringBetween(item, "[", "]");
            Action action = actionName == null ? null : actions.get(actionName.toUpperCase());

            if (action != null) {
                item = item.contains(" ") ? item.split(" ", 2)[1] : "";
                if(MurderMystery.PAPI) item = PlaceholderAPI.setPlaceholders(player, item);
                GamePlayer gamePlayer = PlayerController.getPlayerController().get(player.getUniqueId());
                action.execute(plugin, gamePlayer, item);
            }else{
                plugin.getLogger().warning("There was a problem attempting to process action: '" + item + "'");
            }
        });
    }

}
