package ga.justreddy.wiki.rmurdermystery.nms;

import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface NMSMethods extends Listener {

    void spawnCorpse(Arena arena, Player player, Player players, Location location);

    void destroyCorpse(Arena arena, Player player);

    void hideName(Player player, Player players);

    void showName(Player player);

}
