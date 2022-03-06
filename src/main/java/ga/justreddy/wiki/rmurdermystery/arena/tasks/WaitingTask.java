package ga.justreddy.wiki.rmurdermystery.arena.tasks;

import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import ga.justreddy.wiki.rmurdermystery.arena.events.custom.game.GameStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class WaitingTask extends BukkitRunnable {

    private final Arena arena;

    public WaitingTask(Arena arena){
        this.arena = arena;
    }

    int time = 15;

    @Override
    public void run() {
        if(arena.getGameState().equals(GameState.WAITING)){
            time--;
        }
        if(time <= 0){
            arena.getGameManager().setGameState(arena, GameState.PLAYING);
            Bukkit.getPluginManager().callEvent(new GameStartEvent(arena, ArenaManager.getArenaManager()));
            cancel();
        }
    }

    public int getTime() {
        return time;
    }
}
