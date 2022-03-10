package ga.justreddy.wiki.rmurdermystery.nms.versions.v_Minus_16;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.player.PlayerController;
import ga.justreddy.wiki.rmurdermystery.nms.NMSMethods;
import ga.justreddy.wiki.rmurdermystery.nms.versions.Corpse;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.golde.bukkit.corpsereborn.CorpseAPI.CorpseAPI;
import org.golde.bukkit.corpsereborn.CorpseAPI.events.CorpseClickEvent;
import org.golde.bukkit.corpsereborn.Main;
import org.golde.bukkit.corpsereborn.Util;
import org.golde.bukkit.corpsereborn.nms.Corpses;
import org.golde.bukkit.corpsereborn.nms.NmsBase;
import org.golde.bukkit.corpsereborn.nms.nmsclasses.NMSCorpses_v1_8_R3;

import java.util.HashMap;
import java.util.Map;

public class NMS implements NMSMethods {

    private static final Map<Arena, Corpse> corpseMap = new HashMap<>();

    public NMS(){
        if(!Bukkit.getPluginManager().isPluginEnabled(Main.getPlugin())){
            Bukkit.getConsoleSender().sendMessage("CorpseReborn isn't installed, disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(MurderMystery.getPlugin(MurderMystery.class));
        }
    }

    @Override
    public void spawnCorpse(Arena arena, Player player, Player players, Location location) {
        Corpses.CorpseData corpse = CorpseAPI.spawnCorpse(player, location);
        corpse.setCanSee(players, true);
        corpseMap.put(arena, new Corpse(corpse));
    }

    @Override
    public void destroyCorpse(Arena arena, Player player) {
        for(Map.Entry<Arena, Corpse> corpseEntry : corpseMap.entrySet()){
            System.out.println(corpseEntry.getKey() == arena);
            if(corpseEntry.getKey() == arena){
                for(Corpse corpse : corpseMap.values()){
                    for(Player on : Bukkit.getOnlinePlayers()){
                        ((CraftPlayer) on).getHandle().playerConnection
                                .sendPacket(new PacketPlayOutEntityDestroy(corpse.getCorpseData().getEntityId()));
                    }
                    corpseMap.remove(arena, corpse);

                }
            }
        }

        corpseMap.clear();
    }

    @Override
    public void hideName(Player player, Player players) {

    }

    @Override
    public void showName(Player player) {

    }

    @EventHandler
    public void onCorpseClick(CorpseClickEvent e){
        for(Arena arena : ArenaManager.getArenaManager().getArenas()){
            GamePlayer gamePlayer = PlayerController.getPlayerController().get(e.getClicker().getUniqueId());
            if(arena.getPlayers().contains(gamePlayer)){
                e.setCancelled(true);
                gamePlayer.getPlayer().closeInventory();
            }
        }
    }

    public static Map<Arena, Corpse> getCorpseMap() {
        return corpseMap;
    }
}
