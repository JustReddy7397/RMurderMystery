package ga.justreddy.wiki.rmurdermystery.dependency;

import com.avaje.ebean.text.json.JsonElement;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.dependency.relocation.Relocation;
import ga.justreddy.wiki.rmurdermystery.dependency.relocation.RelocationHandler;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DependencyRegistry {


    private static final SetMultimap<StorageType, Dependency> STORAGE_DEPENDENCIES = ImmutableSetMultimap.<StorageType, Dependency>builder()
            .putAll(StorageType.MONGODB,        Dependency.MONGODB_DRIVER_CORE, Dependency.MONGODB_DRIVER_LEGACY, Dependency.MONGODB_DRIVER_SYNC, Dependency.MONGODB_DRIVER_BSON)
            .putAll(StorageType.MYSQL,          Dependency.SLF4J_API, Dependency.SLF4J_SIMPLE, Dependency.HIKARI, Dependency.MYSQL_DRIVER)
            .putAll(StorageType.H2,             Dependency.H2_DRIVER)
            .build();

    private final MurderMystery plugin;

    public DependencyRegistry(MurderMystery plugin) {
        this.plugin = plugin;
    }

    public Set<Dependency> resolveStorageDependencies(Set<StorageType> storageTypes) {
        Set<Dependency> dependencies = new LinkedHashSet<>();
        for (StorageType storageType : storageTypes) {
            dependencies.addAll(STORAGE_DEPENDENCIES.get(storageType));
        }

        return dependencies;
    }

    public void applyRelocationSettings(Dependency dependency, List<Relocation> relocations) {

        if (!RelocationHandler.DEPENDENCIES.contains(dependency) && isGsonRelocated()) {
            relocations.add(Relocation.of("guava", "com{}google{}common"));
            relocations.add(Relocation.of("gson", "com{}google{}gson"));
        }

    }

    public boolean shouldAutoLoad(Dependency dependency) {
        // all used within 'isolated' classloaders, and are therefore not
        // relocated.
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean isGsonRelocated() {
        return JsonElement.class.getName().startsWith("ga.justreddy.wiki");
    }

    private static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean slf4jPresent() {
        return classExists("org.slf4j.Logger") && classExists("org.slf4j.LoggerFactory");
    }
}
