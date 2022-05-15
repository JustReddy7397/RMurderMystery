package ga.justreddy.wiki.rmurdermystery.commands.subcommands;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.commands.Command;
import ga.justreddy.wiki.rmurdermystery.utils.Utils;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload", "Reloads the plugin", "/mm reload", false, "mm.reload");
    }


    @Override
    public void onCommand(MurderMystery plugin, Player player, String[] args) {

    }

    @SneakyThrows
    @Override
    public void onCommand(MurderMystery plugin, CommandSender sender, String[] args) {
        plugin.getMenuManager().onDisable();
        plugin.getMenuManager().onEnable(plugin);
        plugin.getSignsConfig().reload();
        plugin.getHotbarConfig().reload();
        plugin.getScoreboardConfig().reload();
        plugin.getArenaListConfig().reload();
        plugin.getDatabaseConfig().reload();
        plugin.getKnifeSkinsConfig().reload();
        plugin.getSelectMenuConfig().reload();
        plugin.getStatsMenuConfig().reload();
        sender.sendMessage(Utils.format("&aSuccessfully reloaded the plugin"));
    }
}
