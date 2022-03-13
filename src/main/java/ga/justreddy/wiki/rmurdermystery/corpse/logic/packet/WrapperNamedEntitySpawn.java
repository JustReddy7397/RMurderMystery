package ga.justreddy.wiki.rmurdermystery.corpse.logic.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import ga.justreddy.wiki.rmurdermystery.corpse.util.VersionUtil;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WrapperNamedEntitySpawn implements IPacket {

    private PacketContainer packet;


    private final int id;
    private final UUID uuid;
    private final Location location;

    public WrapperNamedEntitySpawn(int entityID, @NotNull UUID uuid, @NotNull Location location) {
        this.id = entityID;
        this.uuid = uuid;
        this.location = location;
    }

    @Override
    public void load() {
        packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.NAMED_ENTITY_SPAWN);

        /*
            Unknown reason
            1.12.2 client packet error caused by this
            packet = new PacketContainer(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        */

        if(VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            packet.getModifier().writeDefaults();
            packet.getIntegers().
                    write(0, this.id).
                    write(1, (int) (this.location.getX() * 32)).
                    write(2, (int) (this.location.getY() * 32)).
                    write(3, (int) (this.location.getZ() * 32));

            packet.getUUIDs()
                    .write(0, this.uuid);
        } else {
            packet.getIntegers()
                    .write(0, this.id);
            packet.getUUIDs()
                    .write(0, this.uuid);
            packet.getDoubles()
                    .write(0, this.location.getX())
                    .write(1, this.location.getY())
                    .write(2, this.location.getZ());
            packet.getBytes()
                    .write(0, (byte) (this.location.getYaw() * 256.0F / 360.0F))
                    .write(1, (byte) (this.location.getPitch() * 256.0F / 360.0F));
        }

    }

    @Nullable
    @Override
    public PacketContainer get() {
        return packet;
    }

}
