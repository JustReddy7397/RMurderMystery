package ga.justreddy.wiki.rmurdermystery.corpse.logic.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import ga.justreddy.wiki.rmurdermystery.corpse.util.VersionUtil;

public class WrapperEntityMetadata implements IPacket {

    private PacketContainer packet;
    private final int id;

    public WrapperEntityMetadata(int entityID) {
        this.id = entityID;
    }

    @Override
    public void load() {
        packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, this.id);
        WrappedDataWatcher watcher = new WrappedDataWatcher();

        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_13)) {
            WrappedDataWatcher.WrappedDataWatcherObject visible = new WrappedDataWatcher.WrappedDataWatcherObject(6, WrappedDataWatcher.Registry.get(EnumWrappers.getEntityPoseClass()));
            watcher.setObject(visible, EnumWrappers.EntityPose.SLEEPING.toNms());
            /*
            //Works and set location a little higher
            WrappedDataWatcher.WrappedDataWatcherObject bed = new WrappedDataWatcher.WrappedDataWatcherObject(14, WrappedDataWatcher.Registry.getBlockPositionSerializer(true));
            watcher.setObject(bed, Optional.of(BlockPosition.getConverter().getGeneric(new BlockPosition(corpse.location.toVector()))));
             */
            int indexSkinLayer = VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17) ? 17 : 16;
            WrappedDataWatcher.WrappedDataWatcherObject skinLayers = new WrappedDataWatcher.WrappedDataWatcherObject(indexSkinLayer, WrappedDataWatcher.Registry.get(Byte.class));
            watcher.setObject(skinLayers, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40 | 0x80));
        } else {
            watcher.setObject(10, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40 | 0x80));
        }
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
    }

    @Override
    public PacketContainer get() {
        return packet;
    }
}
