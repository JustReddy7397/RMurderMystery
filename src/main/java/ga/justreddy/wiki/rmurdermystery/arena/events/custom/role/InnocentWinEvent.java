package ga.justreddy.wiki.rmurdermystery.arena.events.custom.role;

import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InnocentWinEvent extends Event {

    private static final HandlerList handlersList = new HandlerList();

    private final GamePlayer innocent;
    private final ArenaManager arenaManager;
    private final Arena arena;

    public InnocentWinEvent(GamePlayer innocent, ArenaManager arenaManager, Arena arena){
        this.innocent = innocent;
        this.arenaManager = arenaManager;
        this.arena = arena;
    }

    public GamePlayer getInnocent() {
        return innocent;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public Arena getArena() {
        return arena;
    }

    public static HandlerList getHandlerList() {
        return handlersList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlersList;
    }
}
