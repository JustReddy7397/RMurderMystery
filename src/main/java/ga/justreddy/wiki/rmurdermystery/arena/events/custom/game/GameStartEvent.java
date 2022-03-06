package ga.justreddy.wiki.rmurdermystery.arena.events.custom.game;

import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event {

    private static final HandlerList handlersList = new HandlerList();

    private final Arena arena;
    private final ArenaManager arenaManager;

    public GameStartEvent(Arena arena, ArenaManager arenaManager){
        this.arena = arena;
        this.arenaManager = arenaManager;
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
