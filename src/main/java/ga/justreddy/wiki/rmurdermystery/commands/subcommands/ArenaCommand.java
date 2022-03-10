package ga.justreddy.wiki.rmurdermystery.commands.subcommands;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.commands.Command;
import ga.justreddy.wiki.rmurdermystery.data.ArenaFile;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import wiki.justreddy.ga.reddyutils.uitl.ChatUtil;

import java.io.IOException;

public class ArenaCommand extends Command implements ChatUtil {

    public ArenaCommand() {
        super("arena", "Main command for arenas", "&c/mm arena <arguments>", true, "mm.arena");
    }

    @Override
    public void onCommand(MurderMystery plugin, Player player, String[] args) {
        if(args.length > 1){
            if(args[1].equalsIgnoreCase("create")){
                try{
                    final String arenaName = args[2];

                    int maxPlayers;
                    try{
                        maxPlayers = Integer.parseInt(args[3]);
                    }catch (NumberFormatException ex){
                        player.sendMessage(c(getSyntax()));
                        return;
                    }

                    ArenaFile.createArenaFile(player.getUniqueId(), arenaName, maxPlayers, player.getName());
                }catch (IndexOutOfBoundsException ex){
                    player.sendMessage(c("&cYou are missing arguments!"));
                    return;
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }else if(args[1].equalsIgnoreCase("setlobby")){
                Arena arena = ArenaManager.getArenaManager().getSetup().get(player.getUniqueId());
                Location location = player.getLocation();
                arena.setLobbyLocation(location);
                player.sendMessage(c("&aSuccessfully set the lobby for this arena"));
            }else if (args[1].equalsIgnoreCase("spawn")){
                try{
                    Arena arena = ArenaManager.getArenaManager().getSetup().get(player.getUniqueId());
                    if(args[2].equalsIgnoreCase("player") && args[3].equalsIgnoreCase("add")){
                        ConfigurationSection section = ArenaFile.getArenaFile(arena).getConfigurationSection("spawns.game");
                        int size = section.getKeys(false).size();
                        Location location = player.getLocation();
                        ArenaFile.getArenaFile(arena).set("spawns.game." + (size + 1), location);
                        ArenaFile.saveFile(arena);
                        player.sendMessage(c("&aSuccessfully added a player spawn"));
                    }else if(args[2].equalsIgnoreCase("player") && args[3].equalsIgnoreCase("remove")){
                        ConfigurationSection section = ArenaFile.getArenaFile(arena).getConfigurationSection("spawns.game");
                        int size = section.getKeys(false).size();
                        ArenaFile.getArenaFile(arena).set("spawns.game." + (size - 1), null);
                        ArenaFile.saveFile(arena);
                        player.sendMessage(c("&aSuccessfully removed a player spawn"));
                    }else if(args[2].equalsIgnoreCase("gold") && args[3].equalsIgnoreCase("add")){
                        ConfigurationSection section = ArenaFile.getArenaFile(arena).getConfigurationSection("spawns.gold");
                        int size = section.getKeys(false).size();
                        Location location = player.getLocation();
                        ArenaFile.getArenaFile(arena).set("spawns.gold." + (size + 1), location.getBlock().getLocation());
                        ArenaFile.saveFile(arena);
                        player.sendMessage(c("&aSuccessfully added a gold spawn"));
                    }else if(args[2].equalsIgnoreCase("gold") && args[3].equalsIgnoreCase("remove")) {
                        ConfigurationSection section = ArenaFile.getArenaFile(arena).getConfigurationSection("spawns.gold");
                        int size = section.getKeys(false).size();
                        ArenaFile.getArenaFile(arena).set("spawns.gold." + (size - 1), null);
                        ArenaFile.saveFile(arena);
                        player.sendMessage(c("&aSuccessfully removed a gold spawn"));
                    }
                }catch (IndexOutOfBoundsException ex){
                    player.sendMessage(c("/mm arena spawn <player/gold> <add/remove>"));
                }
            }else if(args[1].equalsIgnoreCase("minPlayers")){
                Arena arena = ArenaManager.getArenaManager().getSetup().get(player.getUniqueId());
                try{
                    int minPlayers;
                    try{
                        minPlayers = Integer.parseInt(args[2]);
                    }catch (NumberFormatException ex){
                        player.sendMessage(c("/mm arena minPlayers <size>"));
                        return;
                    }

                    ArenaFile.getArenaFile(arena).set("minPlayers.", minPlayers);
                    ArenaFile.saveFile(arena);
                    player.sendMessage(c("&aSuccessfully changed the minPlayers for this game to " + minPlayers));
                }catch (IndexOutOfBoundsException ex){
                    player.sendMessage(c("/mm arena minPlayers <size>"));
                }
            }else if(args[1].equalsIgnoreCase("maxPlayers")){
                Arena arena = ArenaManager.getArenaManager().getSetup().get(player.getUniqueId());
                try{
                    int maxPlayers;
                    try{
                        maxPlayers = Integer.parseInt(args[2]);
                    }catch (NumberFormatException ex){
                        player.sendMessage(c("/mm arena maxPlayers <size>"));
                        return;
                    }

                    ArenaFile.getArenaFile(arena).set("maxPlayers.", maxPlayers);
                    ArenaFile.saveFile(arena);
                    player.sendMessage(c("&aSuccessfully changed the maxPlayers for this game to " + maxPlayers));
                }catch (IndexOutOfBoundsException ex){
                    player.sendMessage(c("/mm arena maxPlayers <size>"));
                }
            }else if(args[1].equalsIgnoreCase("save")){
                try {

                    if(plugin.getConfig().get("mainLobby") == null){
                        player.sendMessage(c("&cPlease set the main lobby first! (/mm setmainlobby)"));
                        return;
                    }

                    ArenaFile.saveArena(player.getUniqueId());

                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }else if (args.length == 1){
            player.sendMessage(c("Placeholder Message"));
        }

    }

    @Override
    public void onCommand(MurderMystery plugin, CommandSender sender, String[] args) {
    }

}
