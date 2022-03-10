package ga.justreddy.wiki.rmurdermystery.data;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import ga.justreddy.wiki.rmurdermystery.arena.enums.SignState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ArenaFile {

    private static File file;
    private static FileConfiguration config;


    public static void createArenaFile(UUID uuid, String name, int maxPlayers, String worldName) throws IOException {
        if(ArenaManager.getArenaManager().isInSetup(uuid)){
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "You are already in a setup");
            return;
        }
        file = new File("plugins/" + MurderMystery.getPlugin(MurderMystery.class).getDescription().getName() + "/data/arenas/" + name + ".yml");
        if(file.exists()){
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "An arena with this name already exists");
            return;
        }
        file.createNewFile();
        config = YamlConfiguration.loadConfiguration(file);
        config.set("name", name);
        config.set("displayName", name);
        config.set("enabled", false);
        config.set("setupDone", false);
        config.set("lobby", null);
        config.set("maxPlayers", maxPlayers);
        config.set("minPlayers", 2);
        config.set("worldName", worldName);
        config.set("spawns.game", new HashMap<>());
        config.set("spawns.gold", new HashMap<>());
        try{
            config.save(file);
        }catch (IOException ex){
            ex.printStackTrace();
        }
        Arena arena = new Arena(name, name, GameState.LOBBY, SignState.WAITING, null, maxPlayers, 2);
        ArenaManager.getArenaManager().getSetup().put(uuid, arena);
    }

    public static void saveArena(UUID uuid) throws IOException {
        Arena arena = ArenaManager.getArenaManager().getSetup().get(uuid);
        config.set("enabled", false);
        config.set("setupDone", true);
        config.set("lobby", arena.getLobbyLocation());
        config.save(file);

        ConfigurationSection section = getArenaFile(arena).getConfigurationSection("spawns.game");
        for(String key : section.getKeys(false)){
            Location location = (Location) section.get(key);
            arena.addSpawnLocation(location);
        }
        ConfigurationSection a = getArenaFile(arena).getConfigurationSection("spawns.gold");
        for(String key : a.getKeys(false)){
            Location location = (Location) a.get(key);
            arena.addGoldLocation(location);
        }

        arena.setMaxPlayers(getArenaFile(arena).getInt("maxPlayers"));
        arena.setMaxPlayers(getArenaFile(arena).getInt("minPlayers"));
        ArenaManager.getArenaManager().getArenas().add(arena);
    }

    public static boolean deleteArena(String name){
        file = new File("plugins/" + MurderMystery.getPlugin(MurderMystery.class).getDescription().getName() + "/data/arenas/" + name + ".yml");
        if(!file.exists()) return false;
        file.delete();
        List<String> list = MurderMystery.getPlugin(MurderMystery.class).getConfigManager().getFile("arenalist").getConfig().getStringList("arenas");
        list.remove(file.getName());
        MurderMystery.getPlugin(MurderMystery.class).getConfigManager().saveFile("arenalist");
        return true;
    }

    public static FileConfiguration getArenaFile(Arena arena){
        file = new File("plugins/" + MurderMystery.getPlugin(MurderMystery.class).getDescription().getName() + "/data/arenas/" + arena.getName() + ".yml");
        return config = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveFile(Arena arena){
        file = new File("plugins/" + MurderMystery.getPlugin(MurderMystery.class).getDescription().getName() + "/data/arenas/" + arena.getName() + ".yml");
        try {
            config.save(file);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void cleanUp() {
        File folder = new File("plugins/data/arenas");
        if(!folder.exists()) return;
        if(folder.listFiles() == null) return;
        for(File f : folder.listFiles()){
            config = YamlConfiguration.loadConfiguration(f);
            if(!config.getBoolean("setupDone")) {
                f.delete();
                List<String> list = MurderMystery.getPlugin(MurderMystery.class).getConfigManager().getFile("arenalist").getConfig().getStringList("arenas");
                list.remove(f.getName());
                MurderMystery.getPlugin(MurderMystery.class).getConfigManager().saveFile("arenalist");
            }
        }
    }

}
