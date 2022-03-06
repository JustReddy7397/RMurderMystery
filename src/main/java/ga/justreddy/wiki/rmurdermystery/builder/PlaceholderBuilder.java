package ga.justreddy.wiki.rmurdermystery.builder;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import ga.justreddy.wiki.rmurdermystery.arena.enums.PlayerType;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PlaceholderBuilder {


    public static String setPlaceholders(String text, Arena arena, GamePlayer gamePlayer){
        if(MurderMystery.PAPI ) text = PlaceholderAPI.setPlaceholders(gamePlayer.getPlayer(), text);

        if(text.contains("%date%")){
            DateTimeFormatter globalFormat = DateTimeFormatter.ofPattern(MurderMystery.getPlugin(MurderMystery.class).getConfigManager().getFile("scoreboard").getConfig().getString("pattern"));
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            ZonedDateTime newTime = zonedDateTime.withZoneSameInstant(ZoneId.of(
                    MurderMystery.getPlugin(MurderMystery.class).getConfigManager().getFile("scoreboard").getConfig().getString("date")
            ));

            text = text.replaceAll("%date%", globalFormat.format(newTime));
        }

        if(text.contains("%time%")){
            if(arena.getGameState() == GameState.WAITING && arena.getWaitingTask() != null){
                text = text.replaceAll("%time%", getDurationString(arena.getWaitingTask().getTime()));
            }else if(arena.getGameState() == GameState.PLAYING && arena.getPlayingTask() != null){
                text = text.replaceAll("%time%", getDurationString(arena.getPlayingTask().getTime()));
            }
        }

        if(text.contains("%map_displayname%")) text = text.replaceAll("%map_displayname%", arena.getDisplayName());


        if(text.contains("%map_name%")) text = text.replaceAll("%map_name%", arena.getName());


        if(text.contains("%role%")) text = text.replaceAll("%role%", arena.getGamePlayerType().get(gamePlayer).getName());

        if(text.contains("%innocents%")){
            int i = 0;
            for(PlayerType playerType : arena.getGamePlayerType().values()){
                if(playerType == PlayerType.INNOCENT){
                    i++;
                }
            }
            text = text.replaceAll("%innocents%", String.valueOf(i));
        }

        if(text.contains("%players%")) text = text.replaceAll("%players%", String.valueOf(arena.getPlayersAlive().size()));

        if(text.contains("%max%")) text = text.replaceAll("%max%", String.valueOf(arena.getMaxPlayers()));

        return text;
    }

    public static String setPlaceholders(String text, GamePlayer gamePlayer){
        if(MurderMystery.PAPI ) text = PlaceholderAPI.setPlaceholders(gamePlayer.getPlayer(), text);
        if(text.contains("%date%")){
            DateTimeFormatter globalFormat = DateTimeFormatter.ofPattern(MurderMystery.getPlugin(MurderMystery.class).getConfigManager().getFile("scoreboard").getConfig().getString("pattern"));
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            ZonedDateTime newTime = zonedDateTime.withZoneSameInstant(ZoneId.of(
                    MurderMystery.getPlugin(MurderMystery.class).getConfigManager().getFile("scoreboard").getConfig().getString("date")
            ));

            text = text.replaceAll("%date%", globalFormat.format(newTime));
        }

        return text;
    }

    private static String getDurationString(int seconds) {

        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(minutes) + ":" + twoDigitString(seconds);
    }

    private static String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    private static String c(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
