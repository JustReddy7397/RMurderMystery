package ga.justreddy.wiki.rmurdermystery.arena;

import de.tr7zw.changeme.nbtapi.NBTEntity;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.utils.SignUtil;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import ga.justreddy.wiki.rmurdermystery.arena.enums.SignState;
import ga.justreddy.wiki.rmurdermystery.arena.events.custom.player.GamePlayerJoinEvent;
import ga.justreddy.wiki.rmurdermystery.arena.events.custom.player.GamePlayerLeaveEvent;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.tasks.WaitingTask;
import ga.justreddy.wiki.rmurdermystery.controller.LastWordsController;
import ga.justreddy.wiki.rmurdermystery.corpse.api.CorpseAPI;
import ga.justreddy.wiki.rmurdermystery.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class ArenaManager {

    private static ArenaManager arenaManager;

    public static ArenaManager getArenaManager() {
        if (arenaManager == null) arenaManager = new ArenaManager();
        return arenaManager;
    }

    private final Map<UUID, Arena> setup = new HashMap<>();

    private final List<Arena> arenas = new ArrayList<>();

    public List<Arena> getArenas() {
        return arenas;
    }

    public Arena getArena(String name) {
        Arena arena = null;
        for (Arena a : getArenas()) {
            if (Objects.equals(a.getName(), name)) {
                arena = a;
            }
        }
        return arena;
    }

    public void joinArena(GamePlayer gamePlayer, Arena arena) {

        if(arena == null) {
            Utils.errorCommand(gamePlayer.getPlayer(), "An arena with this name doesn't exist");
            return;
        }

        if (arena.getPlayers().size() >= arena.getMaxPlayers()) {
            Utils.errorCommand(gamePlayer.getPlayer(), "This game is full");
            return;
        }
        if (isInGame(gamePlayer, arena.getName())) {
            Utils.errorCommand(gamePlayer.getPlayer(), "You are already in a game");
            return;
        }

        if(arena.getGameState() == GameState.PLAYING || arena.getGameState() == GameState.ENDING || arena.getGameState() == GameState.RESTARTING){
            Utils.errorCommand(gamePlayer.getPlayer(), "This arena has already started");
            return;
        }


        arena.getPlayers().add(gamePlayer);
        arena.getPlayersAlive().add(gamePlayer);
        arena.getNoRoles().add(gamePlayer);
        Bukkit.getPluginManager().callEvent(new GamePlayerJoinEvent(gamePlayer, this, arena));
        gamePlayer.setSavedArmorContents(gamePlayer.getPlayer().getInventory().getArmorContents());
        gamePlayer.setSavedInventoryContents(gamePlayer.getPlayer().getInventory().getContents());
        if (arena.getPlayers().size() >= arena.getMinPlayers() && arena.getGameState() == GameState.LOBBY) {
            arena.waitingTask = new WaitingTask(arena);
            arena.getWaitingTask().runTaskTimer(MurderMystery.getPlugin(MurderMystery.class), 0, 20L);
            arena.getGameManager().setGameState(arena, GameState.WAITING);
        }
        gamePlayer.getPlayer().getInventory().clear();
        gamePlayer.getPlayer().setFoodLevel(20);
        SignUtil.getSignUtil().update(arena.getName());
    }

    public void leaveArena(GamePlayer gamePlayer) {
        Arena arena = null;
        for (Arena a : getArenas()) {
            if (a.getPlayers().contains(gamePlayer)) {
                arena = a;
            }
        }

        if (arena == null || !isInGame(gamePlayer, arena.getName())) {
            Utils.errorCommand(gamePlayer.getPlayer(), "You're not in a game");
            return;
        }
        arena.getPlayers().remove(gamePlayer);
        arena.getPlayersAlive().remove(gamePlayer);
        arena.getNoRoles().remove(gamePlayer);
        SignUtil.getSignUtil().update(arena.getName());
        Bukkit.getPluginManager().callEvent(new GamePlayerLeaveEvent(gamePlayer, this, arena));
        if (arena.getPlayers().size() < arena.getMinPlayers() && arena.getGameState() == GameState.WAITING) {
            if (arena.getWaitingTask() != null) {
                arena.getWaitingTask().cancel();
                arena.getGameManager().setGameState(arena, GameState.LOBBY);
            }
        }
        gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
        final Location lobby = (Location) MurderMystery.getPlugin(MurderMystery.class).getConfig().get("mainLobby");
        gamePlayer.resetNameTag();
        Bukkit.getScheduler().runTaskLater(MurderMystery.getPlugin(MurderMystery.class), () -> {
            gamePlayer.teleport(lobby);
        }, 20L);
        gamePlayer.giveItems();
    }

    public boolean isInGame(GamePlayer gamePlayer, String arenaName) {
        Arena arena = getArena(arenaName);
        return arena.getPlayers().contains(gamePlayer);
    }

    public void reloadArena(Arena arena) {
        arena.setGameState(GameState.LOBBY);
        arena.setSignState(SignState.WAITING);

        if(arena.getWaitingTask() != null) arena.getWaitingTask().cancel();
        if(arena.getPlayingTask() != null) arena.getPlayingTask().cancel();
        if (arena.getEndingTask() != null) arena.getEndingTask().cancel();
        if(arena.getGoldTask() != null) arena.getGoldTask().cancel();
        for(GamePlayer gamePlayer : arena.getPlayers()) {
            gamePlayer.resetNameTag();
            final Location lobby = (Location) MurderMystery.getPlugin(MurderMystery.class).getConfig().get("mainLobby");
            Bukkit.getScheduler().runTaskLater(MurderMystery.getPlugin(MurderMystery.class), () -> {
                gamePlayer.teleport(lobby);
            }, 20L);
            try{
                LastWordsController.getLastWordsController().getByGamePlayer(gamePlayer).remove();
            }catch (NullPointerException ignored) {}
            gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
            gamePlayer.getPlayer().getInventory().clear();
            gamePlayer.setSavedInventoryContents(gamePlayer.getSavedInventoryContents());
            gamePlayer.setSavedArmorContents(gamePlayer.getSavedArmorContents());
        }
        arena.getPlayers().clear();
        arena.getPlayersAlive().clear();
        arena.getNoRoles().clear();
        SignUtil.getSignUtil().update(arena.getName());
        arena.getCorpses().forEach(corpse -> {
            CorpseAPI.getInstance().removeCorpse(corpse);
        });
        arena.getCorpses().clear();
        SignUtil.getSignUtil().update(arena.getName());
        File f = new File("plugins/" + MurderMystery.getPlugin(MurderMystery.class).getDescription().getName() + "/data/arenas/" + arena.getName() + ".yml");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(f);
        ConfigurationSection section = configuration.getConfigurationSection("spawns.game");
        for (String key : section.getKeys(false)) {
            Location location = (Location) section.get(key);
            arena.addSpawnLocation(location);
        }
        ConfigurationSection b = configuration.getConfigurationSection("spawns.gold");

        for (String key : b.getKeys(false)) {
            Location location = (Location) b.get(key);
            arena.addGoldLocation(location);
        }
        MurderMystery.getPlugin(MurderMystery.class).getLogger().log(Level.INFO, "Successfully reloaded the arena " + arena.getName());

    }

    private Arena createArenas() {
        Arena a = null;
        File folder = new File("plugins/" + MurderMystery.getPlugin(MurderMystery.class).getDescription().getName() + "/data/arenas/");
        if (!folder.exists()) return null;
        for (File f : folder.listFiles()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(f);
            a = new Arena(
                    configuration.getString("name"),
                    configuration.getString("displayName"),
                    GameState.LOBBY, SignState.WAITING,
                    (Location) configuration.get("lobby"), configuration.getInt("maxPlayers"),
                    configuration.getInt("minPlayers"));
            ConfigurationSection section = configuration.getConfigurationSection("spawns.game");
            for (String key : section.getKeys(false)) {
                Location location = (Location) section.get(key);
                a.addSpawnLocation(location);
            }
            ConfigurationSection b = configuration.getConfigurationSection("spawns.gold");
            for (String key : b.getKeys(false)) {
                Location location = (Location) b.get(key);
                a.addGoldLocation(location);
            }

            arenas.add(a);
            break;
        }

        return a;
    }

    public void loadArenas() {
        if (MurderMystery.getPlugin(MurderMystery.class).getArenaListConfig().getConfig().getStringList("arenas").isEmpty())
            return;
        for (String arenaName : MurderMystery.getPlugin(MurderMystery.class).getArenaListConfig().getConfig().getStringList("arenas")) {
            Arena a = createArenas();
            if (a == null) continue;
            Bukkit.getLogger().log(Level.INFO, "Loaded arena: " + a.getName());
            a.name = arenaName;
        }
        System.out.println("Loaded " + arenas.size() + " arenas");
    }

    public Map<UUID, Arena> getSetup() {
        return setup;
    }

    public boolean isInSetup(UUID uuid) {
        return setup.containsKey(uuid);
    }

}
