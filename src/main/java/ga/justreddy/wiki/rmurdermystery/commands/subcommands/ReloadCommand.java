package ga.justreddy.wiki.rmurdermystery.commands.subcommands;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wiki.justreddy.ga.reddyutils.uitl.ChatUtil;

public class ReloadCommand extends Command implements ChatUtil {

    public ReloadCommand() {
        super("reload", "Reloads the plugin", "/mm reload", false, "mm.reload");
    }


    @Override
    public void onCommand(MurderMystery plugin, Player player, String[] args) {

    }

    @Override
    public void onCommand(MurderMystery plugin, CommandSender sender, String[] args) {
        plugin.getMenuManager().onDisable();
        plugin.getMenuManager().onEnable(plugin);
        plugin.getConfigManager().reloadFiles();
        sender.sendMessage(c("&aSuccessfully reloaded the plugin"));
    }
}
