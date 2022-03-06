package ga.justreddy.wiki.rmurdermystery.arena;

import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import ga.justreddy.wiki.rmurdermystery.arena.enums.PlayerType;
import ga.justreddy.wiki.rmurdermystery.arena.enums.SignState;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.tasks.EndingTask;
import ga.justreddy.wiki.rmurdermystery.arena.tasks.GoldTask;
import ga.justreddy.wiki.rmurdermystery.arena.tasks.PlayingTask;
import ga.justreddy.wiki.rmurdermystery.arena.tasks.WaitingTask;
import ga.justreddy.wiki.rmurdermystery.builder.CorpseBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arena {

    public String name;
    public String displayName;
    public List<GamePlayer> players = new ArrayList<>();
    public List<GamePlayer> noRoles = new ArrayList<>();
    public GameState gameState;
    public SignState signState;
    public List<Location> spawnLocations = new ArrayList<>();
    public List<Location> goldLocations = new ArrayList<>();
    public final List<GamePlayer> playersAlive = new ArrayList<>();
    public final Map<GamePlayer, PlayerType> gamePlayerType = new HashMap<>();
    public Location lobbyLocation;
    public int minPlayers;
    public int maxPlayers;
    public GameManager gameManager;
    public WaitingTask waitingTask = null;
    public PlayingTask playingTask = null;
    public EndingTask endingTask = null;
    public GoldTask goldTask = null;
    public List<CorpseBuilder> corpseBuilders = new ArrayList<>();

    public Arena(String name, String displayName, GameState gameState, SignState signState, Location lobbyLocation, int maxPlayers, int minPlayers){
        this.name = name;
        this.displayName = displayName;
        this.gameState = gameState;
        this.signState = signState;
        this.lobbyLocation = lobbyLocation;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.gameManager = new GameManager();
    }

    public String getName() {
        return name;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public List<Location> getGoldLocations() {
        return goldLocations;
    }

    public List<Location> getSpawnLocations() {
        return spawnLocations;
    }

    public SignState getSignState() {
        return signState;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSignState(SignState signState) {
        this.signState = signState;
    }

    public void addSpawnLocation(Location location){
        this.spawnLocations.add(location);
    }

    public void removeSpawnLocation(Location location){
        this.spawnLocations.remove(location);
    }

    public void addGoldLocation(Location location){
        this.goldLocations.add(location);
    }

    public void removeGoldLocation(Location location){
        this.goldLocations.remove(location);
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setWaitingTask(WaitingTask waitingTask) {
        this.waitingTask = waitingTask;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public WaitingTask getWaitingTask() {
        return waitingTask;
    }

    public void setPlayingTask(PlayingTask playingTask) {
        this.playingTask = playingTask;
    }

    public EndingTask getEndingTask() {
        return endingTask;
    }

    public PlayingTask getPlayingTask() {
        return playingTask;
    }

    public GoldTask getGoldTask() {
        return goldTask;
    }

    public Map<GamePlayer, PlayerType> getGamePlayerType() {
        return gamePlayerType;
    }

    public List<GamePlayer> getNoRoles() {
        return noRoles;
    }

    public List<CorpseBuilder> getCorpseBuilders() {
        return corpseBuilders;
    }

    public void sendMessage(String... message){
        for(String msg : message){
            ChatColor.translateAlternateColorCodes('&', msg);
        }
        getPlayers().forEach(gamePlayer -> {
            gamePlayer.sendMessage(message);
        });
    }

    public void sendMessage(String message){
        getPlayers().forEach(gamePlayer ->  {
            gamePlayer.sendMessage(gamePlayer.c(message));
        });
    }

    public List<GamePlayer> getPlayersAlive() {
        return playersAlive;
    }
}
