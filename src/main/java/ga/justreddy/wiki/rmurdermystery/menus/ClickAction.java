package ga.justreddy.wiki.rmurdermystery.menus;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface ClickAction {

    void execute(final Player player);

}
