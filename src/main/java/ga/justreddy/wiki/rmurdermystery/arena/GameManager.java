package ga.justreddy.wiki.rmurdermystery.arena;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import ga.justreddy.wiki.rmurdermystery.arena.tasks.EndingTask;
import ga.justreddy.wiki.rmurdermystery.controller.VictoryDancesController;
import ga.justreddy.wiki.rmurdermystery.cosmetics.VictoryDances;
import sun.java2d.marlin.MarlinRenderingEngine;

public class GameManager {

    public void setGameState(Arena arena, GameState gameState){
        arena.setGameState(gameState);

        switch (gameState){
            case PLAYING:
                if(arena.getWaitingTask() != null) arena.getWaitingTask().cancel();
            case ENDING:
                arena.endingTask = new EndingTask(arena);
                arena.endingTask.runTaskTimer(MurderMystery.getPlugin(MurderMystery.class), 0, 20L);
                break;
            case RESTARTING:
                ArenaManager.getArenaManager().reloadArena(arena);
                break;
        }

    }

}
