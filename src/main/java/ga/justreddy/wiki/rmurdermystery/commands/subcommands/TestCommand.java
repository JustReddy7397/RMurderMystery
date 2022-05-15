package ga.justreddy.wiki.rmurdermystery.commands.subcommands;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.commands.Command;
import ga.justreddy.wiki.rmurdermystery.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TestCommand extends Command {

    public TestCommand() {
        super("test", " test", "test", true, "");
    }

    @Override
    public void onCommand(MurderMystery plugin, Player player, String[] args) {
        try{
            String menuName = args[1];
            List<String> a = new ArrayList<>();
            a.add("[menu] " + menuName);
            MurderMystery.getPlugin(MurderMystery.class).getActionManager().executeActions(player, a);
        }catch (IndexOutOfBoundsException ex){
            Utils.errorCommand(player, "You are missing arguments. /mm test <menuname>");
        }
    }

    @Override
    public void onCommand(MurderMystery plugin, CommandSender sender, String[] args) {

    }


}
