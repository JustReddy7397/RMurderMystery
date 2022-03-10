package ga.justreddy.wiki.rmurdermystery.builder;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rmurdermystery.SignUtil;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.player.PlayerController;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import wiki.justreddy.ga.reddyutils.uitl.ChatUtil;

public class SignBuilder implements Listener, ChatUtil {

    @EventHandler
    public void onSignChange(SignChangeEvent e){

        Player p = e.getPlayer();
        Block b = e.getBlock();

        if(p.hasPermission("mm.sign.create")){
            String firstLine = e.getLine(0);
            String secondLine = e.getLine(1);
            String thirdLine = e.getLine(2);
            Arena arena = ArenaManager.getArenaManager().getArena(thirdLine);
            if (arena == null && firstLine != null && firstLine.equalsIgnoreCase("[RMM]")) {
                b.breakNaturally();
                p.sendMessage(c("&cThat arena does not exist"));
                return;
            }
            if (firstLine != null && firstLine.equalsIgnoreCase("[RMM]") && secondLine != null && secondLine.equalsIgnoreCase("[join]")
                    && thirdLine != null && thirdLine.equalsIgnoreCase(arena.getName())) {
                e.setLine(0, c("&7[&bMurderMystery&7]"));
                e.setLine(1, c("&0Map: &7" + arena.getDisplayName()));
                e.setLine(2, arena.getSignState().getIdentifier());
                e.setLine(3, c("&0" + arena.getPlayers().size() + "/" + arena.getMaxPlayers()));
                p.sendMessage(c("&aSuccessfully placed the sign"));
                SignUtil signutil = new SignUtil(b.getX(), b.getY(), b.getZ(), b.getWorld().getName(), arena.getName());
                signutil.save();
            }
        }
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (b.getType() == XMaterial.OAK_SIGN.parseMaterial() || b.getType() == XMaterial.OAK_WALL_SIGN.parseMaterial()) {
            Sign sign = (Sign) b.getState();
            String arenaName = ChatColor.stripColor(sign.getLine(1));
            String finalArenaName = arenaName.replace("Map: ", "");
            Arena arena = ArenaManager.getArenaManager().getArena(finalArenaName);
            if (arena == null) return;
            if (arena.getPlayers().size() == arena.getMaxPlayers()) {
                p.sendMessage(c("&cThis arena is full"));
                return;
            }

            if (!(arena.getGameState().equals(GameState.LOBBY) || (arena.getGameState() != GameState.WAITING))) {
                p.sendMessage(c("&cThis game has already started"));
                return;
            }


            if (sign.getLine(0).equalsIgnoreCase(c("&7[&bMurderMystery&7]"))) {
                if (sign.getLine(1).equalsIgnoreCase(sign.getLine(1))) {
                    GamePlayer gamePlayer = PlayerController.getPlayerController().get(p.getUniqueId());
                    ArenaManager.getArenaManager().joinArena(gamePlayer, arena);
                }
            }
        }
    }


}
