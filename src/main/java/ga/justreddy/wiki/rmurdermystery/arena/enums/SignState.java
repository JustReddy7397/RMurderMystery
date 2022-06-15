package ga.justreddy.wiki.rmurdermystery.arena.enums;


import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

public enum SignState  {

    WAITING(MurderMystery.getPlugin(MurderMystery.class).getSettingsConfig().getConfig().getString("sign.text.waiting"),
            XMaterial.matchXMaterial(MurderMystery.getPlugin(MurderMystery.class).getSettingsConfig().getConfig().getString("sign.block.waiting")).get()),
    STARTING(MurderMystery.getPlugin(MurderMystery.class).getSettingsConfig().getConfig().getString("sign.text.starting"),
            XMaterial.matchXMaterial(MurderMystery.getPlugin(MurderMystery.class).getSettingsConfig().getConfig().getString("sign.block.starting")).get()),
    PLAYING(MurderMystery.getPlugin(MurderMystery.class).getSettingsConfig().getConfig().getString("sign.text.playing"),
            XMaterial.matchXMaterial(MurderMystery.getPlugin(MurderMystery.class).getSettingsConfig().getConfig().getString("sign.block.playing")).get()),
    ENDING(MurderMystery.getPlugin(MurderMystery.class).getSettingsConfig().getConfig().getString("sign.text.ending"),
            XMaterial.matchXMaterial(MurderMystery.getPlugin(MurderMystery.class).getSettingsConfig().getConfig().getString("sign.block.ending")).get()),
    RESTARTING(MurderMystery.getPlugin(MurderMystery.class).getSettingsConfig().getConfig().getString("sign.text.restarting"),
            XMaterial.matchXMaterial(MurderMystery.getPlugin(MurderMystery.class).getSettingsConfig().getConfig().getString("sign.block.restarting")).get());


    private final String identifier;
    private final XMaterial behindBlock;

    SignState(String identifier, XMaterial behindBlock){
        this.identifier = identifier;
        this.behindBlock = behindBlock;
    }

    public String getIdentifier() {
        return Utils.format(identifier);
    }

    public XMaterial getBehindBlock() {
        return behindBlock;
    }
}
