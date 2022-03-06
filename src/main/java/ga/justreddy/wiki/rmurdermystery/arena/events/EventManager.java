package ga.justreddy.wiki.rmurdermystery.arena.events;

import com.cryptomorin.xseries.XSound;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import ga.justreddy.wiki.rmurdermystery.arena.enums.PlayerType;
import ga.justreddy.wiki.rmurdermystery.arena.events.custom.game.GameStartEvent;
import ga.justreddy.wiki.rmurdermystery.arena.events.custom.player.GamePlayerJoinEvent;
import ga.justreddy.wiki.rmurdermystery.arena.events.custom.player.GamePlayerLeaveEvent;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.player.PlayerController;
import ga.justreddy.wiki.rmurdermystery.arena.tasks.GoldTask;
import ga.justreddy.wiki.rmurdermystery.arena.tasks.PlayingTask;
import ga.justreddy.wiki.rmurdermystery.builder.CorpseBuilder;
import ga.justreddy.wiki.rmurdermystery.controller.KnifeSkinsController;
import ga.justreddy.wiki.rmurdermystery.controller.LastWordsController;
import ga.justreddy.wiki.rmurdermystery.controller.VictoryDancesController;
import ga.justreddy.wiki.rmurdermystery.cosmetics.KnifeSkins;
import ga.justreddy.wiki.rmurdermystery.cosmetics.LastWords;
import ga.justreddy.wiki.rmurdermystery.cosmetics.VictoryDances;
import ga.justreddy.wiki.rmurdermystery.scoreboard.lib.AssembleBoard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import wiki.justreddy.ga.reddyutils.uitl.ChatUtil;

import javax.xml.bind.annotation.XmlSchema;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class EventManager implements Listener, ChatUtil {

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();

        if(e.getAction() != Action.RIGHT_CLICK_AIR) return;
        ConfigurationSection configurationSection = MurderMystery.getPlugin(MurderMystery.class).getConfigManager().getFile("hotbar").getConfig().getConfigurationSection("items");
        if(configurationSection == null) return;
        for(String key : configurationSection.getKeys(false)){
            ConfigurationSection section = MurderMystery.getPlugin(MurderMystery.class).getConfigManager().getFile("hotbar").getConfig().getConfigurationSection("items." + key);
            if(p.getItemInHand() != null && p.getItemInHand().getItemMeta().getDisplayName() != null && p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(c(section.getString("displayname")))){
                if(section.getStringList("actions") != null && !section.getStringList("actions").isEmpty()){
                    MurderMystery.getPlugin(MurderMystery.class).getActionManager().executeActions(p, section.getStringList("actions"));
                }
            }
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PlayerController.getPlayerController().add(e.getPlayer().getUniqueId());
        MurderMystery.getPlugin(MurderMystery.class).getLobbyBoardMap().put(
                PlayerController.getPlayerController().get(e.getPlayer().getUniqueId()),
                new AssembleBoard(e.getPlayer(), MurderMystery.getPlugin(MurderMystery.class).getLobbyBoard()));
        PlayerController.getPlayerController().get(e.getPlayer().getUniqueId()).giveItems();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        GamePlayer gamePlayer = PlayerController.getPlayerController().get(e.getPlayer().getUniqueId());
        MurderMystery.getPlugin(MurderMystery.class).getLobbyBoardMap().remove(gamePlayer);
        for (Arena arena : ArenaManager.getArenaManager().getArenas()) {
            if (arena.getPlayers().contains(gamePlayer)) {
                ArenaManager.getArenaManager().leaveArena(gamePlayer);
                if (arena.getGamePlayerType().get(gamePlayer) == PlayerType.MURDERER) {
                    // TODO win event
                }
            }
        }
    }

    @EventHandler
    public void onGamePlayerJoin(GamePlayerJoinEvent e) {
        e.getArena().sendMessage("&7[&a+&7] &e%player% joined (&a%players%&7/&a%max%)"
                .replaceAll("%player%", e.getGamePlayer().getName())
                .replaceAll("%players%", String.valueOf(e.getArena().getPlayers().size()))
                .replaceAll("%max%", String.valueOf(e.getArena().getMaxPlayers())));
    }

    @EventHandler
    public void onGamePlayerLeave(GamePlayerLeaveEvent e) {
        e.getArena().sendMessage("&7[&c-&7] &e%player% left (&a%players%&7/&a%max%)"
                .replaceAll("%player%", e.getGamePlayer().getName())
                .replaceAll("%players%", String.valueOf(e.getArena().getPlayers().size()))
                .replaceAll("%max%", String.valueOf(e.getArena().getMaxPlayers())));
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        Arena arena = e.getArena();
        ArenaManager arenaManager = e.getArenaManager();
        Random random = new Random();
        arena.playingTask = new PlayingTask(arena);
        arena.playingTask.runTaskTimer(MurderMystery.getPlugin(MurderMystery.class), 0, 20L);
        for (GamePlayer gamePlayer : arena.getPlayers()) {
            if (!arena.getGamePlayerType().containsValue(PlayerType.MURDERER)) {
                arena.getGamePlayerType().put(arena.getNoRoles().remove(random.nextInt(arena.getNoRoles().size())), PlayerType.MURDERER);
            }
            if (arena.getGamePlayerType().get(gamePlayer) == PlayerType.MURDERER) {
                gamePlayer.sendMessage("Murderer");
                KnifeSkins cosmetic = KnifeSkinsController.getKnifeSkinsController().getById(gamePlayer.getCosmetics().getKnifeSkinSelect());
                cosmetic.give(gamePlayer);
            }
            if (!arena.getGamePlayerType().containsValue(PlayerType.DETECTIVE)) {
                arena.getGamePlayerType().put(arena.getNoRoles().remove(random.nextInt(arena.getNoRoles().size())), PlayerType.DETECTIVE);
            }
            if (arena.getGamePlayerType().get(gamePlayer) == PlayerType.DETECTIVE) {
                gamePlayer.sendMessage("Detective");
                gamePlayer.setItem(0, new ItemStack(Material.BOW));
                gamePlayer.setItem(9, new ItemStack(Material.ARROW));
            }

            if(!arena.getGamePlayerType().containsKey(gamePlayer)){
                arena.getGamePlayerType().put(arena.getNoRoles().remove(random.nextInt(arena.getNoRoles().size())), PlayerType.INNOCENT);
                arena.getGamePlayerType().replace(gamePlayer, PlayerType.DEAD, PlayerType.INNOCENT);
            }

            if (arena.getGamePlayerType().get(gamePlayer) == PlayerType.INNOCENT) {
                gamePlayer.sendMessage("Innocent");
            }
            gamePlayer.sendTitle("&7You are a " + arena.getGamePlayerType().get(gamePlayer).getName(), "&7You have to " + arena.getGamePlayerType().get(gamePlayer).getJob());
            gamePlayer.teleport(arena.getSpawnLocations().remove(random.nextInt(arena.getSpawnLocations().size())));
            for (Map.Entry<GamePlayer, PlayerType> a : arena.getGamePlayerType().entrySet()) {
                System.out.println(a.getKey().getName() + " - " + a.getValue().getName());
            }
        }

        arena.goldTask = new GoldTask(arena);
        arena.goldTask.runTaskTimer(MurderMystery.getPlugin(MurderMystery.class), 0, 20L);

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        for (Arena arena : ArenaManager.getArenaManager().getArenas()) {
            if (arena.getPlayersAlive().contains(PlayerController.getPlayerController().get(p.getUniqueId()))) {
                arena.sendMessage(PlayerController.getPlayerController().get(p.getUniqueId()).getName() + ": " + e.getMessage());
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onPlayerKillEvent(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            GamePlayer gamePlayer = PlayerController.getPlayerController().get(player.getUniqueId());
            GamePlayer gamePlayer1 = PlayerController.getPlayerController().get(damager.getUniqueId());
            for (Arena arena : ArenaManager.getArenaManager().getArenas()) {
                if (arena.getPlayers().contains(gamePlayer1) && arena.getPlayers().contains(gamePlayer)) {
                    e.setCancelled(true);
                    if (arena.getGameState() != GameState.PLAYING) return;
                    if (arena.getGamePlayerType().get(gamePlayer1) == PlayerType.MURDERER) {
                        if(arena.getGamePlayerType().get(gamePlayer) == PlayerType.DETECTIVE){
                            for(GamePlayer gamePlayers : arena.getPlayersAlive()){
                                gamePlayers.sendTitle("&6Bow Dropped", "&6The detective has been killed");
                            }
                        }
                        player.setGameMode(GameMode.SPECTATOR);
                        arena.getPlayersAlive().remove(gamePlayer);
                        arena.getGamePlayerType().replace(gamePlayer, arena.getGamePlayerType().get(gamePlayer), PlayerType.DEAD);
                        gamePlayer1.getLocation().getWorld().playSound(gamePlayer.getLocation(), XSound.ENTITY_PLAYER_BIG_FALL.parseSound(), 1L, 1L);
                        CorpseBuilder corpseBuilder = new CorpseBuilder(gamePlayer, gamePlayer.getLocation());
                        arena.getCorpseBuilders().add(corpseBuilder);
                        corpseBuilder.spawn();
                        LastWords lastWords = LastWordsController.getLastWordsController().getByGamePlayer(gamePlayer);
                        lastWords.spawn(gamePlayer);
                        if (arena.getPlayersAlive().size() == 1) {
                            if (arena.getWaitingTask() != null) arena.getWaitingTask().cancel();
                            arena.getGameManager().setGameState(arena, GameState.ENDING);
                            VictoryDances victoryDances = VictoryDancesController.getVictoryDancesController().getById(gamePlayer1.getCosmetics().getVictoryDanceSelect());
                            victoryDances.start(gamePlayer1);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    victoryDances.stop(gamePlayer1);
                                }
                            }.runTaskLater(MurderMystery.getPlugin(MurderMystery.class), 200L);

                            arena.getPlayingTask().cancel();
                            gamePlayer1.sendTitle("&aYou won!", "&aYou've killed all people");
                        }
                    }
                }
            }
        }
        if (e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            Player damager = (Player) ((Arrow) e.getDamager()).getShooter();
            GamePlayer gamePlayer = PlayerController.getPlayerController().get(player.getUniqueId());
            GamePlayer gamePlayer1 = PlayerController.getPlayerController().get(damager.getUniqueId());

            for (Arena arena : ArenaManager.getArenaManager().getArenas()) {
                if (arena.getPlayers().contains(gamePlayer) && arena.getPlayers().contains(gamePlayer1)) {
                    e.setCancelled(true);
                    if (arena.getGameState() != GameState.PLAYING) return;
                    if ((arena.getGamePlayerType().get(gamePlayer) == PlayerType.INNOCENT || arena.getGamePlayerType().get(gamePlayer) == PlayerType.DETECTIVE)
                            && (arena.getGamePlayerType().get(gamePlayer1) == PlayerType.DETECTIVE || arena.getGamePlayerType().get(gamePlayer1) == PlayerType.INNOCENT)) {
                        player.setGameMode(GameMode.SPECTATOR);
                        damager.setGameMode(GameMode.SPECTATOR);
                        arena.getPlayersAlive().remove(gamePlayer);
                        arena.getPlayersAlive().remove(gamePlayer1);
                        arena.getGamePlayerType().replace(gamePlayer, arena.getGamePlayerType().get(gamePlayer), PlayerType.DEAD);
                        arena.getGamePlayerType().replace(gamePlayer1, arena.getGamePlayerType().get(gamePlayer1), PlayerType.DEAD);
                        gamePlayer1.getLocation().getWorld().playSound(gamePlayer.getLocation(), XSound.ENTITY_PLAYER_SMALL_FALL.parseSound(), 1L, 1L);
                        CorpseBuilder corpseBuilder = new CorpseBuilder(gamePlayer, gamePlayer.getLocation());
                        corpseBuilder.spawn();
                        CorpseBuilder corpseBuilder1 = new CorpseBuilder(gamePlayer1, gamePlayer1.getLocation());
                        arena.getCorpseBuilders().add(corpseBuilder1);
                        arena.getCorpseBuilders().add(corpseBuilder);
                        corpseBuilder1.spawn();
                    } else if (arena.getGamePlayerType().get(gamePlayer) == PlayerType.MURDERER && (arena.getGamePlayerType().get(gamePlayer1) == PlayerType.DETECTIVE ||
                            arena.getGamePlayerType().get(gamePlayer1) == PlayerType.INNOCENT)) {
                        player.setGameMode(GameMode.SPECTATOR);
                        arena.getPlayersAlive().remove(gamePlayer);
                        arena.getGamePlayerType().replace(gamePlayer, arena.getGamePlayerType().get(gamePlayer), PlayerType.DEAD);
                        gamePlayer1.getLocation().getWorld().playSound(gamePlayer.getLocation(), XSound.ENTITY_PLAYER_BIG_FALL.parseSound(), 1L, 1L);
                        CorpseBuilder corpseBuilder = new CorpseBuilder(gamePlayer, gamePlayer.getLocation());
                        CorpseBuilder.getCorpseBuilders().put(arena, corpseBuilder);
                        corpseBuilder.spawn();
                        for (GamePlayer gamePlayers : arena.getPlayersAlive()) {
                            VictoryDances victoryDances = VictoryDancesController.getVictoryDancesController().getById(gamePlayers.getCosmetics().getVictoryDanceSelect());
                            victoryDances.start(gamePlayers);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    victoryDances.stop(gamePlayers);
                                }
                            }.runTaskLater(MurderMystery.getPlugin(MurderMystery.class), 100L);
                            gamePlayers.sendTitle("&aYou won!", "&aThe murderer has been killed");
                            arena.getPlayingTask().cancel();
                            arena.getGameManager().setGameState(arena, GameState.ENDING);
                        }

                    }
                }
            }

        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        for (Arena arena : ArenaManager.getArenaManager().getArenas()) {
            if (arena.getPlayers().contains(PlayerController.getPlayerController().get(e.getEntity().getUniqueId()))) {
                e.setFoodLevel(20);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        for (Arena arena : ArenaManager.getArenaManager().getArenas()){
            if (arena.getPlayers().contains(PlayerController.getPlayerController().get(p.getUniqueId()))) {
                e.setCancelled(true);
            }
        }
    }

}
