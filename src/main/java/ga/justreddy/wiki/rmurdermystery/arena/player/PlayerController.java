package ga.justreddy.wiki.rmurdermystery.arena.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerController {

    private static PlayerController playerController;

    private final Map<UUID, GamePlayer> gamePlayers = new HashMap<>();

    public void add(UUID uuid) {
        GamePlayer gamePlayer;
        if (gamePlayers.containsKey(uuid)) return;
        gamePlayer = new GamePlayer(uuid);
        gamePlayers.put(uuid, gamePlayer);
    }

    public GamePlayer remove(UUID uuid) {
        return gamePlayers.remove(uuid);
    }

    public GamePlayer get(UUID uuid){
        return gamePlayers.get(uuid);
    }

    public void shutDown(){
        gamePlayers.clear();
    }

    public Map<UUID, GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public static PlayerController getPlayerController() {
        if (playerController == null) playerController = new PlayerController();
        return playerController;
    }

}
