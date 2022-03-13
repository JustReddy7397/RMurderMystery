package ga.justreddy.wiki.rmurdermystery.corpse.logic.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import ga.justreddy.wiki.rmurdermystery.corpse.util.VersionUtil;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WrapperEntityEquipment implements IPacket {

    private PacketContainer[] packetContainers;

    private final int id;
    private final ItemStack[] armorContents;

    public WrapperEntityEquipment(int entityID, @Nullable ItemStack[] armorContents) {
        this.id = entityID;
        this.armorContents = armorContents;
    }

    @Override
    public void load() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0, this.id);
        if(this.armorContents != null) {
            if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
                packetContainers = new PacketContainer[4];
                ItemStack temp;
                for (int j = 0; j < this.armorContents.length; j++) {
                    temp = this.armorContents[j];
                    if (temp != null) {
                        PacketContainer cloned = packet.deepClone();
                        cloned.getIntegers().write(1, j + 1);
                        cloned.getItemModifier().write(0, this.armorContents[j]);
                        packetContainers[j] = cloned;
                    }
                }
                //remove null packets
                packetContainers = Arrays.stream(packetContainers).filter(Objects::nonNull).toArray(PacketContainer[]::new);
            } else if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_13)) {
                packetContainers = new PacketContainer[4];
                if (this.armorContents[3] != null) {
                    PacketContainer cloned = packet.deepClone();
                    cloned.getItemSlots().write(0, EnumWrappers.ItemSlot.HEAD);
                    cloned.getItemModifier().write(0, this.armorContents[3]);
                    packetContainers[3] = cloned;
                }
                if (this.armorContents[2] != null) {
                    PacketContainer cloned = packet.deepClone();
                    cloned.getItemSlots().write(0, EnumWrappers.ItemSlot.CHEST);
                    cloned.getItemModifier().write(0, this.armorContents[2]);
                    packetContainers[2] = cloned;
                }
                if (this.armorContents[1] != null) {
                    PacketContainer cloned = packet.deepClone();
                    cloned.getItemSlots().write(0, EnumWrappers.ItemSlot.LEGS);
                    cloned.getItemModifier().write(0, this.armorContents[1]);
                    packetContainers[1] = cloned;
                }
                if (this.armorContents[0] != null) {
                    PacketContainer cloned = packet.deepClone();
                    cloned.getItemSlots().write(0, EnumWrappers.ItemSlot.FEET);
                    cloned.getItemModifier().write(0, this.armorContents[0]);
                    packetContainers[0] = cloned;
                }
                //remove null packets
                packetContainers = Arrays.stream(packetContainers).filter(Objects::nonNull).toArray(PacketContainer[]::new);
            } else {
                packetContainers = new PacketContainer[1];
                List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = new ArrayList<>();
                if (this.armorContents[3] != null) {
                    pairList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, this.armorContents[3]));
                }
                if (this.armorContents[2] != null) {
                    pairList.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, this.armorContents[2]));
                }
                if (this.armorContents[1] != null) {
                    pairList.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, this.armorContents[1]));
                }
                if (this.armorContents[0] != null) {
                    pairList.add(new Pair<>(EnumWrappers.ItemSlot.FEET, this.armorContents[0]));
                }
                packet.getSlotStackPairLists().write(0, pairList);
                packetContainers[0] = packet;
            }
        } else {
            packetContainers = new PacketContainer[0];
        }
    }

    @Override
    public PacketContainer get() {
        return null;
    }

    @NotNull
    public PacketContainer[] getMore() {
        return this.packetContainers;
    }
}
