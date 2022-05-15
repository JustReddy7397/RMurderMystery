package ga.justreddy.wiki.rmurdermystery.arena;

import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import ga.justreddy.wiki.rmurdermystery.arena.enums.PlayerType;
import ga.justreddy.wiki.rmurdermystery.arena.enums.SignState;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.tasks.EndingTask;
import ga.justreddy.wiki.rmurdermystery.arena.tasks.GoldTask;
import ga.justreddy.wiki.rmurdermystery.arena.tasks.PlayingTask;
import ga.justreddy.wiki.rmurdermystery.arena.tasks.WaitingTask;
import ga.justreddy.wiki.rmurdermystery.corpse.logic.Corpse;
import ga.justreddy.wiki.rmurdermystery.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arena {


    @Getter @Setter public String name;
    @Getter @Setter public String displayName;
    @Getter @Setter public List<GamePlayer> players = new ArrayList<>();
    @Getter @Setter public List<GamePlayer> noRoles = new ArrayList<>();
    @Getter @Setter public GameState gameState;
    @Getter @Setter public SignState signState;
    @Getter public List<Location> spawnLocations = new ArrayList<>();
    @Getter public List<Location> goldLocations = new ArrayList<>();
    @Getter public final List<GamePlayer> playersAlive = new ArrayList<>();
    @Getter public final Map<GamePlayer, PlayerType> gamePlayerType = new HashMap<>();
    @Getter @Setter public Location lobbyLocation;
    @Getter @Setter public int minPlayers;
    @Getter @Setter public int maxPlayers;
    @Getter @Setter public GameManager gameManager;
    @Getter @Setter public WaitingTask waitingTask = null;
    @Getter @Setter public PlayingTask playingTask = null;
    @Getter @Setter public EndingTask endingTask = null;
    @Getter @Setter public GoldTask goldTask = null;
    @Getter public final List<Corpse> corpses = new ArrayList<>();

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
            gamePlayer.sendMessage(Utils.format(message));
        });
    }

}
