package ga.justreddy.wiki.rmurdermystery;

import ga.justreddy.wiki.rmurdermystery.action.ActionManager;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.events.EventManager;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.player.PlayerController;
import ga.justreddy.wiki.rmurdermystery.builder.DatabaseManager;
import ga.justreddy.wiki.rmurdermystery.builder.MongoBuilder;
import ga.justreddy.wiki.rmurdermystery.builder.SignBuilder;
import ga.justreddy.wiki.rmurdermystery.commands.CommandBase;
import ga.justreddy.wiki.rmurdermystery.controller.KnifeSkinsController;
import ga.justreddy.wiki.rmurdermystery.controller.LastWordsController;
import ga.justreddy.wiki.rmurdermystery.controller.VictoryDancesController;
import ga.justreddy.wiki.rmurdermystery.corpse.logic.Corpse;
import ga.justreddy.wiki.rmurdermystery.corpse.manager.CorpsePool;
import ga.justreddy.wiki.rmurdermystery.cosmetics.lastwords.RageWords;
import ga.justreddy.wiki.rmurdermystery.cosmetics.lastwords.TipWords;
import ga.justreddy.wiki.rmurdermystery.cosmetics.victorydances.FireworkDance;
import ga.justreddy.wiki.rmurdermystery.cosmetics.weapons.Feather;
import ga.justreddy.wiki.rmurdermystery.cosmetics.weapons.IronSword;
import ga.justreddy.wiki.rmurdermystery.data.ArenaFile;
import ga.justreddy.wiki.rmurdermystery.menus.MenuManager;
import ga.justreddy.wiki.rmurdermystery.scoreboard.MurderBoard;
import ga.justreddy.wiki.rmurdermystery.scoreboard.lib.Assemble;
import ga.justreddy.wiki.rmurdermystery.scoreboard.lib.AssembleBoard;
import ga.justreddy.wiki.rmurdermystery.utils.SignUtil;
import ga.justreddy.wiki.rmurdermystery.utils.Utils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;
import pluginlib.DependentJavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class MurderMystery extends DependentJavaPlugin implements Listener {

    @Getter private CommandBase commandBase;

    public static boolean PAPI = false;
    @Getter private ActionManager actionManager;
    @Getter private MenuManager menuManager;
    @Getter private DatabaseManager databaseManager = null;
    @Getter private MongoBuilder mongoBuilder = null;
    // Scoreboard
    @Getter private Assemble lobbyBoard;
    @Getter private final Map<GamePlayer, AssembleBoard> lobbyBoardMap = new HashMap<>();
    @Getter private boolean mongoConnected = false;
    @Getter private CorpsePool pool;
    private final File file = new File("plugins/RMurderMystery/libs/");

    // Config
    @Getter private YamlConfig arenaListConfig;
    @Getter private YamlConfig knifeSkinsConfig;
    @Getter private YamlConfig selectMenuConfig;
    @Getter private YamlConfig statsMenuConfig;
    @Getter private YamlConfig scoreboardConfig;
    @Getter private YamlConfig databaseConfig;
    @Getter private YamlConfig hotbarConfig;
    @Getter private YamlConfig signsConfig;

    // Config Version
    private final int SCOREBOARD_VERSION = 1;
    private final int DATABASE_VERSION = 1;
    private final int HOTBAR_VERSION = 1;

    @SneakyThrows
    @Override
    public void onLoad() {
        if (!file.exists()) file.mkdirs();

    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        if (!loadConfig()) return;
        loadCosmetics();
        pool = CorpsePool.getInstance();
        final File dataFolder = new File("plugins/" + getDescription().getName() + "/data/arenas");
        if (!dataFolder.exists()) dataFolder.mkdirs();
        final File menuFolder = new File("plugins/" + getDescription().getName() + "/menus/");
        if (!menuFolder.exists()) menuFolder.mkdirs();
        commandBase = new CommandBase();
        getCommand("murdermystery").setExecutor(commandBase);
        ArenaManager.getArenaManager().loadArenas();
        getServer().getPluginManager().registerEvents(new EventManager(), this);
        getServer().getPluginManager().registerEvents(new SignBuilder(), this);
        actionManager = new ActionManager(this);
        menuManager = new MenuManager();
        menuManager.onEnable(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI().register();
            PAPI = true;
        }
        lobbyBoard = new Assemble(this, new MurderBoard());
        switch (getDatabaseConfig().getConfig().getString("storage").toLowerCase()) {
            case "mongodb":
                mongoBuilder = new MongoBuilder(getDatabaseConfig().getConfig().getString("mongodb.uri"));
                mongoConnected = true;
                break;
            case "sql":
                databaseManager = new DatabaseManager();
                databaseManager.connectH2(this, "data/database");
                break;
            case "mysql":
                databaseManager = new DatabaseManager();
                databaseManager.connectMysQL(
                        getDatabaseConfig().getConfig().getString("mysql.database"),
                        getDatabaseConfig().getConfig().getString("mysql.user"),
                        getDatabaseConfig().getConfig().getString("mysql.password"),
                        getDatabaseConfig().getConfig().getString("mysql.host"),
                        getDatabaseConfig().getConfig().getInt("mysql.port")
                );
                break;
        }


    }

    @SneakyThrows
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ArenaFile.cleanUp();
        Bukkit.getScoreboardManager().getMainScoreboard().getTeams().clear();
        PlayerController.getPlayerController().shutDown();
        ArenaManager.getArenaManager().getSetup().clear();
        ArenaManager.getArenaManager().getArenas().clear();
        KnifeSkinsController.getKnifeSkinsController().shutDown();
        VictoryDancesController.getVictoryDancesController().shutDown();
        menuManager.onDisable();
        SignUtil.getSignUtil().updateAll();
        Bukkit.getScheduler().cancelTasks(this);
        for (Player player : Bukkit.getOnlinePlayers()) {
            getLobbyBoardMap().remove(PlayerController.getPlayerController().remove(player.getUniqueId()));

        }
        getLobbyBoardMap().clear();
        BukkitTask task = pool.getTickTask();
        if (task != null) {
            task.cancel();
        }
        for (Corpse c : pool.getCorpses()) {
            c.getSeeingPlayers()
                    .forEach(c::hide);
        }
    }

    private boolean loadConfig() {

        String currentlyLoading = "configuration file";

        try{
            currentlyLoading = "arena_list.yml";
            arenaListConfig = new YamlConfig("data/" + currentlyLoading);
            currentlyLoading = "knifeskins.yml";
            knifeSkinsConfig = new YamlConfig("menus/" + currentlyLoading);
            currentlyLoading = "selectmenu.yml";
            selectMenuConfig = new YamlConfig("menus/" + currentlyLoading);
            currentlyLoading = "statsmenu.yml";
            statsMenuConfig = new YamlConfig("menus/" + currentlyLoading);
            currentlyLoading = "scoreboard.yml";
            scoreboardConfig = new YamlConfig(currentlyLoading);
            if(scoreboardConfig.isOutdated(SCOREBOARD_VERSION)) {
                Utils.error(null, "Outdated scoreboard.yml file.", true);
                return false;
            }
            currentlyLoading = "database.yml";
            databaseConfig = new YamlConfig(currentlyLoading);
            if(databaseConfig.isOutdated(DATABASE_VERSION)) {
                Utils.error(null, "Outdated database.yml file.", true);
                return false;
            }
            currentlyLoading = "hotbar.yml";
            hotbarConfig = new YamlConfig(currentlyLoading);
            if (hotbarConfig.isOutdated(HOTBAR_VERSION)) {
                Utils.error(null, "Outdated hotbar.yml file.", true);
                return false;
            }
            currentlyLoading = "signs.yml";
            signsConfig = new YamlConfig("data/" + currentlyLoading);
        }catch (IOException | InvalidConfigurationException ex) {
            Utils.error(ex, "Failed to load config: " + currentlyLoading, true);
            return false;
        }

        return true;
    }

    private void loadCosmetics() {
        KnifeSkinsController.getKnifeSkinsController().getKnifeskins().put("ironsword", new IronSword());
        KnifeSkinsController.getKnifeSkinsController().getKnifeskins().put("feather", new Feather());
        VictoryDancesController.getVictoryDancesController().getVictoryDances().put("firework", new FireworkDance());
        LastWordsController.getLastWordsController().getLastWords().put("rage", new RageWords());
        LastWordsController.getLastWordsController().getLastWords().put("tips", new TipWords());
    }

    public PluginManager getPluginManager() {
        return Bukkit.getPluginManager();
    }

}
