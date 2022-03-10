package ga.justreddy.wiki.rmurdermystery.dependency;

import lombok.NonNull;
import org.jetbrains.annotations.Unmodifiable;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface Platform {

    /**
     * Gets the type of platform LuckPerms is running on
     *
     * @return the type of platform LuckPerms is running on
     */
    Platform.@NonNull Type getType();

    /**
     * Gets the unique players which have connected to the server since it started.
     *
     * @return the unique connections
     */
    @NonNull @Unmodifiable Set<UUID> getUniqueConnections();

    /**
     * Gets a {@link Collection} of all known permission strings.
     *
     * @return a collection of the known permissions
     */
    @NonNull @Unmodifiable
    Collection<String> getKnownPermissions();

    /**
     * Gets the time when the plugin first started.
     *
     * @return the enable time
     */
    @NonNull Instant getStartTime();

    /**
     * Represents a type of platform which LuckPerms can run on.
     */
    enum Type {
        BUKKIT("Bukkit");
        private final String friendlyName;

        Type(String friendlyName) {
            this.friendlyName = friendlyName;
        }

        /**
         * Gets a readable name for the platform type.
         *
         * @return a readable name
         */
        public @NonNull String getFriendlyName() {
            return this.friendlyName;
        }
    }

}
