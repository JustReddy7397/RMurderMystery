package ga.justreddy.wiki.rmurdermystery.arena.tasks;

import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class EndingTask extends BukkitRunnable {

    private final Arena arena;

    public EndingTask(Arena arena){
        this.arena = arena;
    }

    int time = 10;

    @Override
    public void run() {
        if(arena.getGameState() == GameState.ENDING){
            time--;
        }

        if(time <= 0){
            cancel();
            if(arena.getEndingTask() != null) arena.getEndingTask().cancel();
            arena.getGameManager().setGameState(arena, GameState.RESTARTING);
        }

    }

    public int getTime() {
        return time;
    }
}
