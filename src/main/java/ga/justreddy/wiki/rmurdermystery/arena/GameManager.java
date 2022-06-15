package ga.justreddy.wiki.rmurdermystery.arena;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import ga.justreddy.wiki.rmurdermystery.arena.enums.SignState;
import ga.justreddy.wiki.rmurdermystery.arena.tasks.EndingTask;
import ga.justreddy.wiki.rmurdermystery.utils.SignUtil;

public class GameManager {

    public void setGameState(Arena arena, GameState gameState){
        arena.setGameState(gameState);

        switch (gameState){
            case LOBBY:
                arena.setSignState(SignState.WAITING);
                SignUtil.getSignUtil().update(arena.getName());
                break;
            case PLAYING:
                if(arena.getWaitingTask() != null) arena.getWaitingTask().cancel();
                arena.setSignState(SignState.PLAYING);
                SignUtil.getSignUtil().update(arena.getName());
                break;
            case WAITING:
                arena.setSignState(SignState.STARTING);
                SignUtil.getSignUtil().update(arena.getName());
                break;
            case ENDING:
                arena.endingTask = new EndingTask(arena);
                arena.endingTask.runTaskTimer(MurderMystery.getPlugin(MurderMystery.class), 0, 20L);
                arena.setSignState(SignState.ENDING);
                SignUtil.getSignUtil().update(arena.getName());
                break;
            case RESTARTING:
                arena.setSignState(SignState.RESTARTING);
                SignUtil.getSignUtil().update(arena.getName());
                ArenaManager.getArenaManager().reloadArena(arena);
                break;
        }

    }

}
