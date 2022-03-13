package ga.justreddy.wiki.rmurdermystery.corpse.logic;

import ga.justreddy.wiki.rmurdermystery.corpse.logic.packet.*;
import ga.justreddy.wiki.rmurdermystery.corpse.util.VersionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

public class PacketLoader {

    private final Collection<IPacket> packets = new CopyOnWriteArraySet<>();
    private final WrapperEntityDestroy wrapperEntityDestroy;
    private final WrapperEntityEquipment wrapperEntityEquipment;
    private final WrapperEntityMetadata wrapperEntityMetadata;
    private final WrapperNamedEntitySpawn wrapperNamedEntitySpawn;
    private final WrapperPlayerInfo wrapperPlayerInfoAdd;
    private final WrapperPlayerInfo wrapperPlayerInfoRemove;
    private final WrapperBed wrapperBed;
    private final WrapperEntityTeleport wrapperEntityTeleport;

    public PacketLoader(@NotNull Corpse corpse) {
        //init packets
        this.wrapperEntityDestroy = new WrapperEntityDestroy(corpse.id);
        this.wrapperEntityEquipment = new WrapperEntityEquipment(corpse.id, corpse.armorContents);
        this.wrapperEntityMetadata = new WrapperEntityMetadata(corpse.id);
        this.wrapperNamedEntitySpawn = new WrapperNamedEntitySpawn(corpse.id, corpse.uuid, corpse.location);
        this.wrapperPlayerInfoAdd = new WrapperPlayerInfo(true, corpse.profile, corpse.name);
        this.wrapperPlayerInfoRemove = new WrapperPlayerInfo(false, corpse.profile, corpse.name);
        this.wrapperBed = new WrapperBed(corpse.id, corpse.location);
        this.wrapperEntityTeleport = new WrapperEntityTeleport(corpse.id, corpse.location);
    }

    public void load() {
        //load packets
        packets.add(wrapperEntityDestroy);
        packets.add(wrapperEntityEquipment);
        packets.add(wrapperEntityMetadata);
        packets.add(wrapperNamedEntitySpawn);
        packets.add(wrapperPlayerInfoAdd);
        packets.add(wrapperPlayerInfoRemove);

        //Fix 'Caused by: java.lang.IllegalArgumentException: Could not find packet for type BED' for last versions
        if(VersionUtil.isBelow(VersionUtil.VersionEnum.V1_12)) {
            packets.add(wrapperBed);
            packets.add(wrapperEntityTeleport);
        }
        this.packets.forEach(IPacket::load);
    }

    public WrapperEntityDestroy getWrapperEntityDestroy() {
        return wrapperEntityDestroy;
    }

    public WrapperEntityEquipment getWrapperEntityEquipment() {
        return wrapperEntityEquipment;
    }

    public WrapperEntityMetadata getWrapperEntityMetadata() {
        return wrapperEntityMetadata;
    }

    public WrapperNamedEntitySpawn getWrapperNamedEntitySpawn() {
        return wrapperNamedEntitySpawn;
    }

    public WrapperPlayerInfo getWrapperPlayerInfoAdd() {
        return wrapperPlayerInfoAdd;
    }

    public WrapperPlayerInfo getWrapperPlayerInfoRemove() {
        return wrapperPlayerInfoRemove;
    }

    public WrapperBed getWrapperBed() {
        return wrapperBed;
    }

    public WrapperEntityTeleport getWrapperEntityTeleport() {
        return wrapperEntityTeleport;
    }

}
