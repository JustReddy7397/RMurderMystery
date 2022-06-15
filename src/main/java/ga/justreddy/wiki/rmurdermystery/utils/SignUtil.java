package ga.justreddy.wiki.rmurdermystery.utils;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
public class SignUtil {

    private static SignUtil signUtil;

    private int x;
    private int y;
    private int z;
    private String world;
    private String arena;

    public SignUtil(int x, int y, int z, String world, String arena) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.arena = arena;
    }

    public SignUtil() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getArena() {
        return arena;
    }

    public void setArena(String arena) {
        this.arena = arena;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public void save() {
        FileConfiguration config = MurderMystery.getPlugin(MurderMystery.class).getSignsConfig().getConfig();
        config.set("signs." + getArena() + ".arena", arena);
        config.set("signs." + getArena() + ".world", world);
        config.set("signs." + getArena() + ".x", x);
        config.set("signs." + getArena() + ".y", y);
        config.set("signs." + getArena() + ".z", z);
        try{
            MurderMystery.getPlugin(MurderMystery.class).getSignsConfig().save();
        }catch (IOException ignored) {}
    }

    public void remove() {
        // TODO
    }

    public void updateAll(){
        final ConfigurationSection section = MurderMystery.getPlugin(MurderMystery.class).getSignsConfig().getConfig().getConfigurationSection("signs");
        for(String key : section.getKeys(false)) update(key);
    }

    public void update(String arenaName) {
        final ConfigurationSection section = MurderMystery.getPlugin(MurderMystery.class).getSignsConfig().getConfig().getConfigurationSection("signs");
        if(section == null || section.getKeys(false).isEmpty()) return;
        int finalX = MurderMystery.getPlugin(MurderMystery.class).getSignsConfig().getConfig().getInt("signs." + arenaName + ".x");
        int finalY = MurderMystery.getPlugin(MurderMystery.class).getSignsConfig().getConfig().getInt("signs." + arenaName + ".y");
        int finalZ = MurderMystery.getPlugin(MurderMystery.class).getSignsConfig().getConfig().getInt("signs." + arenaName + ".z");
        World finalWorld = Bukkit.getWorld(MurderMystery.getPlugin(MurderMystery.class).getSignsConfig().getConfig().getString("signs." + arenaName + ".world"));
        if (finalWorld != null) {
            Block block = finalWorld.getBlockAt(finalX, finalY, finalZ);
            if (block != null) {
                final Sign sign = (Sign) block.getState();
                if (sign.getLine(1).equalsIgnoreCase(Utils.format("Map: &7" + arenaName))) {
                    Arena arena = ArenaManager.getArenaManager().getArena(arenaName);
                    int playerCount = arena.getPlayers().size();
                    int maxPlayers = arena.getMaxPlayers();
                    sign.setLine(2, arena.getSignState().getIdentifier());
                    sign.setLine(3, playerCount + "/" + maxPlayers);
                    sign.update(true);
                    if (MurderMystery.getPlugin(MurderMystery.class).getSettingsConfig().getConfig().getBoolean("sign.block.enabled")) {
                        Block attachedBlock = getBlockSignAttachedTo(block);
                        if(attachedBlock == null) return;
                        attachedBlock.setType(arena.getSignState().getBehindBlock().parseMaterial());
                    }
                }
            }


        }
    }

    private Block getBlockSignAttachedTo(Block block) {
        if (block.getType() == XMaterial.OAK_WALL_SIGN.parseMaterial())
            switch (block.getData()) {
                case 2:
                    return block.getRelative(BlockFace.SOUTH);
                case 3:
                    return block.getRelative(BlockFace.NORTH);
                case 4:
                    return block.getRelative(BlockFace.EAST);
                case 5:
                    return block.getRelative(BlockFace.WEST);
            }
        return null;
    }

    public static SignUtil getSignUtil() {
        if (signUtil == null) {
            signUtil = new SignUtil();
        }
        return signUtil;
    }


}
