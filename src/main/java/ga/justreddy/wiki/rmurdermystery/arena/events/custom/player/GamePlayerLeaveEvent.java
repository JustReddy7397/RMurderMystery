package ga.justreddy.wiki.rmurdermystery.arena.events.custom.player;

import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GamePlayerLeaveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final GamePlayer gamePlayer;
    private final ArenaManager arenaManager;
    private final Arena arena;

    public GamePlayerLeaveEvent(GamePlayer gamePlayer, ArenaManager arenaManager, Arena arena){
        this.gamePlayer = gamePlayer;
        this.arenaManager = arenaManager;
        this.arena = arena;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public Arena getArena() {
        return arena;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }



}
