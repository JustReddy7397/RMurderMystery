package ga.justreddy.wiki.rmurdermystery.corpse.logic.packet;

import com.comphenix.protocol.events.PacketContainer;
import org.jetbrains.annotations.Nullable;

public interface IPacket {

    void load();

    @Nullable
    PacketContainer get();
}
