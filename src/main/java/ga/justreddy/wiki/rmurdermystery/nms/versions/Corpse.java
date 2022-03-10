package ga.justreddy.wiki.rmurdermystery.nms.versions;

import net.minecraft.server.level.EntityPlayer;
import org.golde.bukkit.corpsereborn.nms.Corpses;

public class Corpse {

    private Corpses.CorpseData corpseData;

    private EntityPlayer corpsePlayer_1_17;

    public Corpse(Corpses.CorpseData corpses){
        this.corpseData = corpses;
    }

    public Corpses.CorpseData getCorpseData() {
        return corpseData;
    }

    public EntityPlayer getCorpsePlayer_1_17() {
        return corpsePlayer_1_17;
    }

    public Corpse(EntityPlayer corpsePlayer){
        this.corpsePlayer_1_17 = corpsePlayer;
    }



}
