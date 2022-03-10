package ga.justreddy.wiki.rmurdermystery.nms.versions;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.nms.NMSMethods;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NMS_1_17 implements NMSMethods {

    private final Map<Arena, Corpse> corpseMap = new HashMap<>();

    @Override
    public void spawnCorpse(Arena arena, Player player, Player players, Location location) {

         EntityPlayer craftPlayer = ((CraftPlayer) player).getHandle();

        Property property = (Property) craftPlayer.getProfile().getProperties().get("textures").toArray()[0];
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
        gameProfile.getProperties().put("textures", new Property("textures", property.getValue(), property.getSignature()));

        EntityPlayer corpse = new EntityPlayer(
                ((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) player.getWorld()).getHandle(),
                gameProfile
        );

        corpse.setPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

        Location bed = player.getLocation().add(1, 0, 0);

        corpse.e(new BlockPosition(bed.getX(), bed.getY(), bed.getZ()));

        ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), player.getName());
        team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.b);
        PacketPlayOutScoreboardTeam score1 = PacketPlayOutScoreboardTeam.a(team);
        PacketPlayOutScoreboardTeam score2 = PacketPlayOutScoreboardTeam.a(team, true);
        PacketPlayOutScoreboardTeam score3 = PacketPlayOutScoreboardTeam.a(team, corpse.getName(), PacketPlayOutScoreboardTeam.a.a);

        corpse.setPose(EntityPose.c);

        DataWatcher watcher = corpse.getDataWatcher();

        final byte b = 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40;

        watcher.set(DataWatcherRegistry.a.a(17), b);

        PacketPlayOutEntity.PacketPlayOutRelEntityMove move = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                corpse.getId(), (byte) 0, (byte)((player.getLocation().getY() - 1.7 - player.getLocation().getY())  * 32),
                (byte) 0, false
        );

        final PlayerConnection connection = ((CraftPlayer)players).getHandle().b;

        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, corpse));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(corpse));

        connection.sendPacket(score1);
        connection.sendPacket(score2);
        connection.sendPacket(score3);

        connection.sendPacket(new PacketPlayOutEntityMetadata(corpse.getId(), watcher, true));
        connection.sendPacket(move);

        new BukkitRunnable(){
            @Override
            public void run() {
                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, corpse));
            }
        }.runTaskAsynchronously(MurderMystery.getPlugin(MurderMystery.class));

        corpseMap.put(arena, new Corpse(corpse));

    }

    @Override
    public void destroyCorpse(Arena arena, Player player) {
        for(Map.Entry<Arena, Corpse> corpseEntry : corpseMap.entrySet()){
            if(corpseEntry.getKey() == arena){
                for(Corpse corpse : corpseMap.values()){
                    for(Player on : Bukkit.getOnlinePlayers()){
                        ((CraftPlayer) on).getHandle().b
                                .sendPacket(new PacketPlayOutEntityDestroy(corpse.getCorpsePlayer_1_17().getId()));
                    }
                    corpseMap.remove(arena, corpse);
                }
            }
        }
    }

    @Override
    public void hideName(Player player, Player players) {

    }

    @Override
    public void showName(Player player) {

    }

    public Map<Arena, Corpse> getCorpseMap() {
        return corpseMap;
    }
}
