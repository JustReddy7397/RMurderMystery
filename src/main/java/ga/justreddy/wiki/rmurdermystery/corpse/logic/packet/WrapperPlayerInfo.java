package ga.justreddy.wiki.rmurdermystery.corpse.logic.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WrapperPlayerInfo implements IPacket {

    private PacketContainer packet;
    private final boolean add;
    private final WrappedGameProfile gameProfile;
    private final String name;

    public WrapperPlayerInfo(boolean add, @NotNull WrappedGameProfile gameProfile, @NotNull String name) {
        this.add = add;
        this.gameProfile = gameProfile;
        this.name = name;
    }

    @Override
    public void load() {
        packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction()
                .write(0, this.add ? EnumWrappers.PlayerInfoAction.ADD_PLAYER : EnumWrappers.PlayerInfoAction.REMOVE_PLAYER );

        PlayerInfoData data = new PlayerInfoData(this.gameProfile, 1, EnumWrappers.NativeGameMode.CREATIVE, WrappedChatComponent.fromText(this.name));
        List<PlayerInfoData> dataList = new ArrayList<PlayerInfoData>();
        dataList.add(data);

        packet.getPlayerInfoDataLists()
                .write(0, dataList);
    }

    @Override
    public PacketContainer get() {
        return packet;
    }
}
