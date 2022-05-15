package ga.justreddy.wiki.rmurdermystery.commands;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.commands.subcommands.*;
import ga.justreddy.wiki.rmurdermystery.utils.Utils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBase implements CommandExecutor {

    private final List<Command> commands = new ArrayList<>();


    public CommandBase(){
        commands.addAll(Arrays.asList(
                new TestCommand(),
                new ReloadCommand(),
                new ArenaCommand(),
                new JoinCommand(),
                new MainLobbyCommand(),
                new LeaveCommand(),
                new ForceStartCommand()
        ));
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {

        if(args.length > 0){
            for (int i = 0; i < getCommands().size(); i++) {
                Command command = getCommands().get(i);
                if (command.isPlayersOnly()) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Utils.format("&cThis command is player only!"));
                        return true;
                    }
                    Player player = (Player) sender;

                    if ((args[0].equalsIgnoreCase(command.getName())) || (command.getAliases().contains(args[0]))) {
                        if (!command.getPermission().isEmpty() && !sender.hasPermission(command.getPermission())) {
                            Utils.errorCommand(player, "Missing the %permission% permission".replaceAll("%permission%", command.getPermission()));
                            return true;
                        }
                        command.onCommand(MurderMystery.getPlugin(MurderMystery.class), player, args);
                    }
                } else {

                    if ((args[0].equalsIgnoreCase(command.getName())) || (command.getAliases().contains(args[0]))) {
                        command.onCommand(MurderMystery.getPlugin(MurderMystery.class), sender, args);
                    }

                }
            }
        }else if(args.length == 0){
            sender.sendMessage(Utils.format("Placeholder Message"));
        }

        return true;
    }

    public List<Command> getCommands() {
        return commands;
    }


}
