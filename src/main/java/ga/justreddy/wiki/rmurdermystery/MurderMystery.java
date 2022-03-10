package ga.justreddy.wiki.rmurdermystery;

import ga.justreddy.wiki.rmurdermystery.action.ActionManager;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.events.EventManager;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.player.PlayerController;
import ga.justreddy.wiki.rmurdermystery.builder.MongoBuilder;
import ga.justreddy.wiki.rmurdermystery.builder.SignBuilder;
import ga.justreddy.wiki.rmurdermystery.commands.CommandBase;
import ga.justreddy.wiki.rmurdermystery.controller.KnifeSkinsController;
import ga.justreddy.wiki.rmurdermystery.controller.LastWordsController;
import ga.justreddy.wiki.rmurdermystery.controller.VictoryDancesController;
import ga.justreddy.wiki.rmurdermystery.cosmetics.lastwords.RageWords;
import ga.justreddy.wiki.rmurdermystery.cosmetics.victorydances.FireworkDance;
import ga.justreddy.wiki.rmurdermystery.cosmetics.weapons.Feather;
import ga.justreddy.wiki.rmurdermystery.cosmetics.weapons.IronSword;
import ga.justreddy.wiki.rmurdermystery.data.ArenaFile;
import ga.justreddy.wiki.rmurdermystery.dependency.DependencyDownloadException;
import ga.justreddy.wiki.rmurdermystery.dependency.DependencyManager;
import ga.justreddy.wiki.rmurdermystery.menus.MenuManager;
import ga.justreddy.wiki.rmurdermystery.nms.NMSMethods;
import ga.justreddy.wiki.rmurdermystery.nms.versions.NMS_1_18;
import ga.justreddy.wiki.rmurdermystery.nms.versions.NMS_1_17;
import ga.justreddy.wiki.rmurdermystery.nms.versions.v_Minus_16.NMS;
import ga.justreddy.wiki.rmurdermystery.scoreboard.MurderBoard;
import ga.justreddy.wiki.rmurdermystery.scoreboard.lib.Assemble;
import ga.justreddy.wiki.rmurdermystery.scoreboard.lib.AssembleBoard;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import wiki.justreddy.ga.reddyutils.config.ConfigManager;
import wiki.justreddy.ga.reddyutils.dependency.DLoader;
import wiki.justreddy.ga.reddyutils.dependency.base.Dependency;
import wiki.justreddy.ga.reddyutils.manager.DatabaseManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class MurderMystery extends JavaPlugin implements Listener {

    private ConfigManager configManager;
    private CommandBase commandBase;

    public static boolean PAPI = false;
    private static final Dependency MONGO_DEPENDENCY_DRIVER = new Dependency("mongodb-driver", "3.12.10", "org.mongodb", "mongodb-driver");
    private static final Dependency MONGO_DEPENDENCY_DRIVER_CORE = new Dependency("mongodb-driver-core", "3.12.10", "org.mongodb", "mongodb-driver-core");
    private static final Dependency BSON = new Dependency("BSON", "4.4.0", "org.mongodb", "bson");
    private static final Dependency H2 = new Dependency("H2", "1.4.200", "com.h2database", "h2");

    private ActionManager actionManager;
    private MenuManager menuManager;
    private DatabaseManager databaseManager = null;
    private MongoBuilder mongoBuilder = null;

    // Scoreboard
    private Assemble lobbyBoard;
    private final Map<GamePlayer, AssembleBoard> lobbyBoardMap = new HashMap<>();
    private boolean mongoConnected = false;

    private NMSMethods nms = null;

    @Override
    public void onLoad() {
/*        DLoader.getInstance().onLoad(this);
        DLoader.getInstance().load(MONGO_DEPENDENCY_DRIVER);
        DLoader.getInstance().load(MONGO_DEPENDENCY_DRIVER_CORE);
        DLoader.getInstance().load(BSON);
        DLoader.getInstance().load(H2);*/


    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        configManager = new ConfigManager();
        configManager.createFolder(this);
        final File dataFolder = new File("plugins/" + getDescription().getName() + "/data/arenas");
        if (!dataFolder.exists()) dataFolder.mkdirs();
        final File menuFolder = new File("plugins/" + getDescription().getName() + "/menus/");
        if (!menuFolder.exists()) menuFolder.mkdirs();
        File file = new File("plugins/" + getDescription().getName() + "/libs");
        if (!file.exists()) file.mkdir();
        DependencyManager dependencyManager = new DependencyManager(this);
        Bukkit.getScheduler().runTaskLater(
                this,
                () -> {
                    try {
                        dependencyManager.downloadDependency(
                                ga.justreddy.wiki.rmurdermystery.dependency.Dependency.H2_DRIVER
                                );
                        dependencyManager.downloadDependency(
                                ga.justreddy.wiki.rmurdermystery.dependency.Dependency.MONGODB_DRIVER_BSON
                                );
                        dependencyManager.downloadDependency(
                                ga.justreddy.wiki.rmurdermystery.dependency.Dependency.MONGODB_DRIVER_CORE
                                );
                        dependencyManager.downloadDependency(
                                ga.justreddy.wiki.rmurdermystery.dependency.Dependency.MONGODB_DRIVER_LEGACY
                                );
                        dependencyManager.downloadDependency(
                                ga.justreddy.wiki.rmurdermystery.dependency.Dependency.MONGODB_DRIVER_SYNC
                                );
                    } catch (DependencyDownloadException e) {
                        e.printStackTrace();
                    }
                }, 5L
        );
        KnifeSkinsController.getKnifeSkinsController().getKnifeskins().put("ironsword", new IronSword());
        KnifeSkinsController.getKnifeSkinsController().getKnifeskins().put("feather", new Feather());
        VictoryDancesController.getVictoryDancesController().getVictoryDances().put("firework", new FireworkDance());
        LastWordsController.getLastWordsController().getLastWords().put("testing", new RageWords());
        configManager.registerFile(this, "arenalist", "data/arena_list");
        configManager.registerFile(this, "knifeskins", "menus/knifeskins");
        configManager.registerFile(this, "selectmenu", "menus/selectmenu");
        configManager.registerFile(this, "statsmenu", "menus/statsmenu");
        configManager.registerFile(this, "scoreboard", "scoreboard");
        configManager.registerFile(this, "database", "database");
        configManager.registerFile(this, "hotbar", "hotbar");
        configManager.registerFile(this, "signs", "data/signs");
        commandBase = new CommandBase();
        getCommand("murdermystery").setExecutor(commandBase);
        System.out.println(commandBase.getCommands().size());
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
        switch (getConfigManager().getFile("database").getConfig().getString("storage").toLowerCase()) {
            case "mongodb":
                mongoBuilder = new MongoBuilder(getConfigManager().getFile("database").getConfig().getString("mongodb.uri"));
                mongoConnected = true;
                break;
            case "sql":
                databaseManager = new DatabaseManager();
                databaseManager.connectH2(this, "data/database");
                break;
            case "mysql":
                databaseManager = new DatabaseManager();
                databaseManager.connectMysQL(
                        getConfigManager().getFile("database").getConfig().getString("mysql.database"),
                        getConfigManager().getFile("database").getConfig().getString("mysql.user"),
                        getConfigManager().getFile("database").getConfig().getString("mysql.password"),
                        getConfigManager().getFile("database").getConfig().getString("mysql.host"),
                        getConfigManager().getFile("database").getConfig().getInt("mysql.port")
                );
                break;
        }
        setupNMS();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ArenaFile.cleanUp();
        for (Arena arena : ArenaManager.getArenaManager().getArenas()) {
            arena.getCorpseBuilders().clear();
        }
        PlayerController.getPlayerController().shutDown();
        ArenaManager.getArenaManager().getSetup().clear();
        ArenaManager.getArenaManager().getArenas().clear();
        KnifeSkinsController.getKnifeSkinsController().shutDown();
        VictoryDancesController.getVictoryDancesController().shutDown();
        menuManager.onDisable();
        SignUtil.getSignUtil().updateAll();
        Bukkit.getScheduler().cancelTasks(this);
        getLobbyBoardMap().clear();
    }

    private void setupNMS() {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_8_R3":
            case "v1_9_R2":
            case "v1_10_R1":
            case "v1_11_R1":
            case "v1_12_R1":
            case "v1_13_R2":
            case "v1_14_R1":
            case "v1_15_R1":
            case "v1_16_R3":
                nms = new NMS();
                Bukkit.getPluginManager().registerEvents(nms, this);
                Bukkit.getConsoleSender().sendMessage(getCommandBase().c("&a[RMurderMystery] Found NMS version for " + version + " enabling corpses..."));
                break;
            case "v1_17_R1":
                nms = new NMS_1_17();
                break;
            case "v1_18_R1":
                nms = new NMS_1_18();
            default:
                Bukkit.getConsoleSender().sendMessage(getCommandBase().c("&c[RMurderMystery] Can't find NMS version for " + version + ", disabling plugin..."));
                getPluginManager().disablePlugin(this);
                break;

        }
    }


    public PluginManager getPluginManager() {
        return Bukkit.getPluginManager();
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public CommandBase getCommandBase() {
        return commandBase;
    }

    public Assemble getLobbyBoard() {
        return lobbyBoard;
    }

    public Map<GamePlayer, AssembleBoard> getLobbyBoardMap() {
        return lobbyBoardMap;
    }

    public boolean isMongoConnected() {
        return mongoConnected;
    }

    public MongoBuilder getMongoBuilder() {
        return mongoBuilder;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public NMSMethods getNms() {
        return nms;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
