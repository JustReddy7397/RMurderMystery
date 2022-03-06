package ga.justreddy.wiki.rmurdermystery.arena.events.custom.player;

import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GamePlayerJoinEvent extends Event {

    private static final HandlerList handlersList = new HandlerList();

    private final GamePlayer gamePlayer;
    private final ArenaManager arenaManager;
    private final Arena arena;

    public GamePlayerJoinEvent(GamePlayer gamePlayer, ArenaManager arenaManager, Arena arena){
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
        return handlersList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlersList;
    }



}
