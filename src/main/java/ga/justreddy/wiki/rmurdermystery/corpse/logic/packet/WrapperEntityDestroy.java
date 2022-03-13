package ga.justreddy.wiki.rmurdermystery.corpse.logic.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import ga.justreddy.wiki.rmurdermystery.corpse.util.VersionUtil;

import java.util.Collections;

public class WrapperEntityDestroy implements IPacket {

    private PacketContainer packet;

    private final int id;

    public WrapperEntityDestroy(int entityID) {
        this.id = entityID;
    }

    @Override
    public void load() {
        packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        if(VersionUtil.isBelow(VersionUtil.VersionEnum.V1_16)) {
            packet.getIntegerArrays().write(0, new int[] { this.id });
        }else{
            packet.getIntLists().write(0, Collections.singletonList(this.id));
        }
    }

    @Override
    public PacketContainer get() {
        return packet;
    }
}
