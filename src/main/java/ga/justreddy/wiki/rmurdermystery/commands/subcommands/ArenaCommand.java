package ga.justreddy.wiki.rmurdermystery.commands.subcommands;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.commands.Command;
import ga.justreddy.wiki.rmurdermystery.data.ArenaFile;
import ga.justreddy.wiki.rmurdermystery.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ArenaCommand extends Command {

    public ArenaCommand() {
        super("arena", "Main command for arenas", "&c/mm arena <arguments>", true, "mm.arena");
    }

    @Override
    public void onCommand(MurderMystery plugin, Player player, String[] args) {
        switch (args[1]) {
            case "create": runCreateCommand(player, args); break;
            case "setlobby": runSetLobbyCommand(player, args); break;
            case "spawn": runSpawnCommand(player, args); break;
            case "minPlayers": runMinPlayersCommand(player, args); break;
            case "maxPlayers": runMaxPlayersCommand(player, args); break;
            case "save": runSaveCommand(player, args); break;
            default: player.sendMessage("Placeholder Message");
        }

    }

    @Override
    public void onCommand(MurderMystery plugin, CommandSender sender, String[] args) {
    }

    private void runCreateCommand(Player player, String[] args) {
        try{
            final String arenaName = args[2];

            int maxPlayers;
            try{
                maxPlayers = Integer.parseInt(args[3]);
            }catch (NumberFormatException ex){
                player.sendMessage(Utils.format(getSyntax()));
                return;
            }

            ArenaFile.createArenaFile(player.getUniqueId(), arenaName, maxPlayers, player.getName());
        }catch (IndexOutOfBoundsException ex){
            Utils.errorCommand(player, "You are missing arguments. Type /mm help for help");
            return;
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void runSetLobbyCommand(Player player, String[] args) {
        Arena arena = ArenaManager.getArenaManager().getSetup().get(player.getUniqueId());
        Location location = player.getLocation();
        arena.setLobbyLocation(location);
        player.sendMessage(Utils.format("&aSuccessfully set the lobby for this arena"));
    }

    private void runSpawnCommand(Player player, String[] args) {
        try{
            Arena arena = ArenaManager.getArenaManager().getSetup().get(player.getUniqueId());
            if(args[2].equalsIgnoreCase("player") && args[3].equalsIgnoreCase("add")){
                ConfigurationSection section = ArenaFile.getArenaFile(arena).getConfigurationSection("spawns.game");
                int size = section.getKeys(false).size();
                Location location = player.getLocation();
                ArenaFile.getArenaFile(arena).set("spawns.game." + (size + 1), location);
                ArenaFile.saveFile(arena);
                player.sendMessage(Utils.format("&aSuccessfully added a player spawn"));
            }else if(args[2].equalsIgnoreCase("player") && args[3].equalsIgnoreCase("remove")){
                ConfigurationSection section = ArenaFile.getArenaFile(arena).getConfigurationSection("spawns.game");
                int size = section.getKeys(false).size();
                ArenaFile.getArenaFile(arena).set("spawns.game." + (size - 1), null);
                ArenaFile.saveFile(arena);
                player.sendMessage(Utils.format("&aSuccessfully removed a player spawn"));
            }else if(args[2].equalsIgnoreCase("gold") && args[3].equalsIgnoreCase("add")){
                ConfigurationSection section = ArenaFile.getArenaFile(arena).getConfigurationSection("spawns.gold");
                int size = section.getKeys(false).size();
                Location location = player.getLocation();
                ArenaFile.getArenaFile(arena).set("spawns.gold." + (size + 1), location.getBlock().getLocation());
                ArenaFile.saveFile(arena);
                player.sendMessage(Utils.format("&aSuccessfully added a gold spawn"));
            }else if(args[2].equalsIgnoreCase("gold") && args[3].equalsIgnoreCase("remove")) {
                ConfigurationSection section = ArenaFile.getArenaFile(arena).getConfigurationSection("spawns.gold");
                int size = section.getKeys(false).size();
                ArenaFile.getArenaFile(arena).set("spawns.gold." + (size - 1), null);
                ArenaFile.saveFile(arena);
                player.sendMessage(Utils.format("&aSuccessfully removed a gold spawn"));
            }
        }catch (IndexOutOfBoundsException ex){
            Utils.errorCommand(player, "You are missing arguments. Type /mm help for help");
        }
    }

    private void runMinPlayersCommand(Player player, String[] args) {
        Arena arena = ArenaManager.getArenaManager().getSetup().get(player.getUniqueId());
        try{
            int minPlayers;
            try{
                minPlayers = Integer.parseInt(args[2]);
            }catch (NumberFormatException ex){
                Utils.errorCommand(player, "minPlayers must ba number!");
                return;
            }

            ArenaFile.getArenaFile(arena).set("minPlayers.", minPlayers);
            ArenaFile.saveFile(arena);
            player.sendMessage(Utils.format("&aSuccessfully changed the minPlayers for this game to " + minPlayers));
        }catch (IndexOutOfBoundsException ex){
            Utils.errorCommand(player, "You are missing arguments. Type /mm help for help");
        }
    }

    private void runMaxPlayersCommand(Player player, String[] args) {
        Arena arena = ArenaManager.getArenaManager().getSetup().get(player.getUniqueId());
        try{
            int maxPlayers;
            try{
                maxPlayers = Integer.parseInt(args[2]);
            }catch (NumberFormatException ex){
                Utils.errorCommand(player, "maxPlayers must be a number!");
                return;
            }

            ArenaFile.getArenaFile(arena).set("maxPlayers.", maxPlayers);
            ArenaFile.saveFile(arena);
            player.sendMessage(Utils.format("&aSuccessfully changed the maxPlayers for this game to " + maxPlayers));
        }catch (IndexOutOfBoundsException ex){
            Utils.errorCommand(player, "You are missing arguments. Type /mm help for help");
        }
    }

    private void runSaveCommand(Player player, String[] args) {
        try {

            if(MurderMystery.getPlugin(MurderMystery.class).getConfig().get("mainLobby") == null){
                Utils.errorCommand(player, "The main lobby has not been set!");
                return;
            }

            ArenaFile.saveArena(player.getUniqueId());

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }



}
