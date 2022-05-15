package ga.justreddy.wiki.rmurdermystery.arena.events.custom.role;

import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MurderWinEvent extends Event {

    private static final HandlerList handlersList = new HandlerList();

    private final GamePlayer murderer;
    private final ArenaManager arenaManager;
    private final Arena arena;

    public MurderWinEvent(GamePlayer murderer, ArenaManager arenaManager, Arena arena){
        this.murderer = murderer;
        this.arenaManager = arenaManager;
        this.arena = arena;
    }

    public GamePlayer getMurderer() {
        return murderer;
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
