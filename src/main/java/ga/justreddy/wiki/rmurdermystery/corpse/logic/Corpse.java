package ga.justreddy.wiki.rmurdermystery.corpse.logic;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.corpse.manager.CorpsePool;
import ga.justreddy.wiki.rmurdermystery.corpse.util.BedUtil;
import ga.justreddy.wiki.rmurdermystery.corpse.util.ProfileUtils;
import ga.justreddy.wiki.rmurdermystery.corpse.util.VersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class Corpse {
    protected final int id;

    protected final UUID uuid;

    protected final String name;

    protected final Location location;

    protected final WrappedGameProfile profile;

    private final Collection<Player> seeingPlayers = new CopyOnWriteArraySet<>();

    private final PacketLoader packetLoader;

    private final CorpsePool pool;

    private boolean armor = false;
    protected ItemStack[] armorContents;


    @ApiStatus.Internal
    public Corpse(
            @NotNull Location location,
            @NotNull WrappedGameProfile wrappedGameProfile,
            @Nullable ItemStack[] armorContents,
            @Nullable String name
    ) {
        pool = CorpsePool.getInstance();

        this.id = pool.getFreeEntityId();
        this.uuid = new UUID(new Random().nextLong(), 0);
        this.name = name == null ? ProfileUtils.randomName() : name;
        this.location = location;
        this.profile = new WrappedGameProfile(this.uuid, this.name);
        //set skin to profile WrappedGameProfile
        wrappedGameProfile.getProperties().get("textures").forEach(property -> profile.getProperties().put("textures", property));

        if(pool.isRenderArmor() && armorContents != null) {
            this.armorContents = armorContents.clone();
            this.armor = this.armorContents[0] != null || this.armorContents[1] != null || this.armorContents[2] != null || this.armorContents[3] != null;
        }
        //load packets
        packetLoader = new PacketLoader(this);
        packetLoader.load();

        //pool take care
        pool.takeCareOf(this);

        //remove eventually corpse after X seconds
        int time = pool.getTimeRemove();
        if(time > -1) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(MurderMystery.getPlugin(MurderMystery.class), ()-> pool.remove(this.id), 20L*time);
        }

    }

    public Corpse(@NotNull Player player) {
        this(player.getLocation(), WrappedGameProfile.fromPlayer(player), player.getInventory().getArmorContents(), player.getName());
    }

    public Corpse(
            @NotNull Location location,
            @NotNull OfflinePlayer offlinePlayer,
            @Nullable ItemStack[] armorContents
    ) {
        this(location, WrappedGameProfile.fromOfflinePlayer(offlinePlayer), armorContents, offlinePlayer.getName());
    }

    @ApiStatus.Internal
    public void show(@NotNull Player player) {
        this.seeingPlayers.add(player);

        if(!this.pool.isShowTags()) {
            hideNameTag(player);
        }
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, this.packetLoader.getWrapperPlayerInfoAdd().get());
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, this.packetLoader.getWrapperNamedEntitySpawn().get());
            // Set sleep
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, this.packetLoader.getWrapperEntityMetadata().get());
            if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_12)) {
                player.sendBlockChange(BedUtil.getBedLocation(location), Material.valueOf("BED_BLOCK"), (byte) BedUtil.yawToFacing(location.getYaw()));
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, this.packetLoader.getWrapperBed().get());
                // Set the correct height of the player lying down
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, this.packetLoader.getWrapperEntityTeleport().get());
            }

            if(armor) {
                for(PacketContainer packet: this.packetLoader.getWrapperEntityEquipment().getMore()) {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
                }
            }

            Bukkit.getScheduler().runTaskLaterAsynchronously(MurderMystery.getPlugin(MurderMystery.class), ()->{
                try {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, this.packetLoader.getWrapperPlayerInfoRemove().get());
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }, 2L);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @ApiStatus.Internal
    public void hide(@NotNull Player player) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, this.packetLoader.getWrapperEntityDestroy().get());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if(!this.pool.isShowTags()) {
            showNameTag(player);
        }
        this.seeingPlayers.remove(player);
    }

    public boolean isShownFor(@NotNull Player player) {
        return this.seeingPlayers.contains(player);
    }

    private static final String TEAM_NAME = "corpse-lib";

    private void hideNameTag(@NotNull Player player) {
        // hide nametag to player
        org.bukkit.scoreboard.Scoreboard scoreboard = player.getScoreboard();
        Team npcs = null;
        for(Team team : scoreboard.getTeams()) {
            if(team.getName().equals(Corpse.TEAM_NAME)) {
                npcs = team;
                break;
            }
        }
        if(npcs == null) {
            npcs = scoreboard.registerNewTeam(Corpse.TEAM_NAME);
        }
        npcs.setNameTagVisibility(NameTagVisibility.NEVER);
        npcs.addEntry(this.name);
    }

    private void showNameTag(@NotNull Player player) {
        // show nametag to player
        player.getScoreboard().getTeams()
                .stream()
                .filter(team -> team.getName().equals(Corpse.TEAM_NAME))
                .forEach(team -> team.removeEntry(this.name));
    }

    public int getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public Location getLocation() {
        return location;
    }

    @NotNull
    public Collection<Player> getSeeingPlayers() {
        return Collections.unmodifiableCollection(this.seeingPlayers);
    }


}
