package ga.justreddy.wiki.rmurdermystery.arena.tasks;

import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayingTask extends BukkitRunnable {

    private final Arena arena;

    public PlayingTask(Arena arena){
        this.arena = arena;
    }

    int time = 600;

    @Override
    public void run() {
        if(arena.getGameState() == GameState.PLAYING){
            time--;
        }
        if(time <= 0){
            arena.getGameManager().setGameState(arena, GameState.ENDING);
            cancel();
        }
    }


    public int getTime() {
        return time;
    }
}
