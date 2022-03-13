package ga.justreddy.wiki.rmurdermystery.corpse.util;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ProfileUtils {

    /**
     * Creates a random name which is exactly 16 chars long and only contains alphabetic and numeric
     * chars.
     * @return a randomly created minecraft name.
     */
    @NotNull
    public static String randomName() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

}
