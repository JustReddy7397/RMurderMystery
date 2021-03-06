package ga.justreddy.wiki.rmurdermystery.utils;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class Utils {

    private Utils() {}

    private static final int CENTER_PX = 154;
    public static final String CHAT_LINE = "&m-----------------------------------------------------";
    public static final String CONSOLE_LINE = "*-----------------------------------------------------*";
    public static final String LORE_LINE = "&m--------------------------";

    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static List<String> formatList(List<String> input) {
        List<String> list = new ArrayList<>();
        for (String line : input) list.add(format(line));
        return list;
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%line%", CHAT_LINE)));
    }

    public static void sendMessage(Player player, String... message) {
        for (String line : message) {
            sendMessage(player, line);
        }
    }


    public static void error(Throwable throwable, String description, boolean disable) {
        if (throwable != null) throwable.printStackTrace();

        if (disable) {
            sendConsole(
                    "&4%line%",
                    "&cAn internal error has occurred in " + MurderMystery.getPlugin(MurderMystery.class).getName() + "!",
                    "&cContact the plugin author if you cannot fix this error.",
                    "&cDescription: &6" + description,
                    "&cThe plugin will now disable.",
                    "&4%line%"
            );
        } else {
            sendConsole(
                    "&4%line%",
                    "&cAn internal error has occurred in " + MurderMystery.getPlugin(MurderMystery.class).getDescription().getName() + "!",
                    "&cContact the plugin author if you cannot fix this error.",
                    "&cDescription: &6" + description,
                    "&4%line%"
            );
        }

        if (disable && Bukkit.getPluginManager().isPluginEnabled(MurderMystery.getPlugin(MurderMystery.class))) {
            Bukkit.getPluginManager().disablePlugin(MurderMystery.getPlugin(MurderMystery.class));
        }
    }

    public static void errorCommand(Player player, String description) {
        sendMessage(player, "&4%line%", "&cAn error occurred while running this command", "&cDescription: &6" + description, "&4%line%");
    }

    public static void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(format(message.replace("%line%", CONSOLE_LINE)));
    }

    public static void sendConsole(String... message) {
        for (String line : message) {
            sendConsole(line);
        }
    }



}
