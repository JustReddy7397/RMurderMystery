package ga.justreddy.wiki.rmurdermystery.builder;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.enums.SignState;
import ga.justreddy.wiki.rmurdermystery.utils.SignUtil;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.player.PlayerController;
import ga.justreddy.wiki.rmurdermystery.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignBuilder implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent e) {

        Player p = e.getPlayer();
        Block b = e.getBlock();


        if (p.hasPermission("mm.sign.create")) {
            String firstLine = e.getLine(0);
            String secondLine = e.getLine(1);
            String thirdLine = e.getLine(2);
            Arena arena = ArenaManager.getArenaManager().getArena(thirdLine);
            if (arena == null && firstLine != null && firstLine.equalsIgnoreCase("[RMM]")) {
                b.breakNaturally();
                p.sendMessage(Utils.format("&cThat arena does not exist"));
                return;
            }
            if (firstLine != null && firstLine.equalsIgnoreCase("[RMM]") && secondLine != null && secondLine.equalsIgnoreCase("[join]")
                    && thirdLine != null && thirdLine.equalsIgnoreCase(arena.getName())) {
                e.setLine(0, Utils.format("&7[&bMurderMystery&7]"));
                e.setLine(1, Utils.format("&0Map: &7" + arena.getDisplayName()));
                e.setLine(2, arena.getSignState().getIdentifier());
                e.setLine(3, Utils.format("&0" + arena.getPlayers().size() + "/" + arena.getMaxPlayers()));
                p.sendMessage(Utils.format("&aSuccessfully placed the sign"));
                SignUtil signutil = new SignUtil(b.getX(), b.getY(), b.getZ(), b.getWorld().getName(), arena.getName());
                signutil.save();
                if (MurderMystery.getPlugin(MurderMystery.class).getSettingsConfig().getConfig().getBoolean("sign.block.enabled")) {
                    Block attachedBlock = getBlockSignAttachedTo(b);
                    if (attachedBlock == null) return;
                    attachedBlock.setType(SignState.WAITING.getBehindBlock().parseMaterial());
                }
            }
        }
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (b.getType() == XMaterial.OAK_SIGN.parseMaterial() || b.getType() == XMaterial.OAK_WALL_SIGN.parseMaterial()) {
            Sign sign = (Sign) b.getState();
            String arenaName = ChatColor.stripColor(sign.getLine(1));
            String finalArenaName = arenaName.replace("Map: ", "");
            Arena arena = ArenaManager.getArenaManager().getArena(finalArenaName);
            if (arena == null) return;
            if (sign.getLine(0).equalsIgnoreCase(Utils.format("&7[&bMurderMystery&7]"))) {
                if (sign.getLine(1).equalsIgnoreCase(sign.getLine(1))) {
                    GamePlayer gamePlayer = PlayerController.getPlayerController().get(p.getUniqueId());
                    ArenaManager.getArenaManager().joinArena(gamePlayer, arena);
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
}
