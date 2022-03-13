package ga.justreddy.wiki.rmurdermystery.corpse.api;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import ga.justreddy.wiki.rmurdermystery.corpse.logic.Corpse;
import ga.justreddy.wiki.rmurdermystery.corpse.manager.CorpsePool;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CorpseAPI {

    private static CorpseAPI instance;

    public CorpseAPI() {
        throw new IllegalArgumentException();
    }

    private CorpseAPI(Object dummy) { }

    /**
     * Method that creates a corpse in the player's position and with its skin and inventory
     * @param player The player to copy
     * @return a new Corpse object
     */
    public Corpse spawnCorpse(@NotNull Player player) {
        Validate.notNull(player, "Player cannot be null");
        return new Corpse(player);
    }

    /**
     * Method that creates a corpse in the given place and with the skin, name and inventory of the player
     * @param player The player to copy
     * @param location The location where to spawn the corpse
     * @return a new Corpse object
     */
    public Corpse spawnCorpse(@NotNull Player player, @NotNull Location location) {
        Validate.notNull(player, "Player cannot be null");
        Validate.notNull(location, "Spawn location cannot be null");
        return new Corpse(location, WrappedGameProfile.fromPlayer(player), null, player.getName());
    }

    /**
     * Method that creates a corpse in the given place and with the skin and name of the offlinePlayer
     * @param offlinePlayer The offlinePlayer to copy
     * @param location The location where to spawn the corpse
     * @return a new Corpse object
     */
    public Corpse spawnCorpse(@NotNull OfflinePlayer offlinePlayer, @NotNull Location location) {
        Validate.notNull(offlinePlayer, "OfflinePlayer cannot be null");
        Validate.notNull(location, "Spawn location cannot be null");
        return new Corpse(location, offlinePlayer, null);
    }

    /**
     * Method that creates a corpse in the given place and with the skin and name of the player
     * with a custom inventory.
     * @param player The player to copy
     * @param location The location where to spawn the corpse
     * @param helmet The helmet to put on the corpse
     * @param chestPlate The chestPlate to put on the corpse
     * @param leggings The leggings to put on the corpse
     * @param boots The boots to put on the corpse
     * @return a new Corpse object
     */
    public Corpse spawnCorpse(
            @NotNull Player player,
            @NotNull Location location,
            @Nullable ItemStack helmet,
            @Nullable ItemStack chestPlate,
            @Nullable ItemStack leggings,
            @Nullable ItemStack boots
    ) {
        Validate.notNull(player, "Player cannot be null");
        Validate.notNull(location, "Spawn location cannot be null");
        return new Corpse(location, WrappedGameProfile.fromPlayer(player), new ItemStack[]{boots, leggings, chestPlate, helmet}, player.getName());
    }

    /**
     * Method that creates a corpse in the given place and with the skin and name of the offlinePlayer
     * with a custom inventory.
     * @param offlinePlayer The offlinePlayer to copy
     * @param location The location where to spawn the corpse
     * @param helmet The helmet to put on the corpse
     * @param chestPlate The chestPlate to put on the corpse
     * @param leggings The leggings to put on the corpse
     * @param boots The boots to put on the corpse
     * @return a new Corpse object
     */
    public Corpse spawnCorpse(
            @NotNull OfflinePlayer offlinePlayer,
            @NotNull Location location,
            @Nullable ItemStack helmet,
            @Nullable ItemStack chestPlate,
            @Nullable ItemStack leggings,
            @Nullable ItemStack boots
    ) {
        Validate.notNull(offlinePlayer, "OfflinePlayer cannot be null");
        Validate.notNull(location, "Spawn location cannot be null");
        return new Corpse(location, offlinePlayer, new ItemStack[]{boots, leggings, chestPlate, helmet});
    }

    /**
     * Method that removes a corpse
     * @param corpse The corpse to be removed
     */
    public void removeCorpse(@NotNull Corpse corpse) {
        Validate.notNull(corpse, "Corpse cannot be null");
        CorpsePool.getInstance().remove(corpse.getId());
    }

    /**
     * Class method that allows you to use the API.
     * @return an instance of this class
     */
    @NotNull
    public static synchronized CorpseAPI getInstance() {
        if(instance == null) {
            instance = new CorpseAPI(null);
        }
        return instance;
    }

}
