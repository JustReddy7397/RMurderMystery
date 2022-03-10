package ga.justreddy.wiki.rmurdermystery.menus;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.menus.custom.CustomGUI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuManager {
    private MurderMystery plugin;

    private final Map<String, AbstractInventory> inventories;

    public MenuManager() {
        inventories = new HashMap<>();
    }

    public void onEnable(MurderMystery plugin) {
        this.plugin = plugin;

        loadCustomMenus();

        inventories.values().forEach(AbstractInventory::onEnable);

        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(), plugin);
    }

    private void loadCustomMenus() {

        File directory = new File("plugins/" + plugin.getDescription().getName() + "/menus/");

        if (!directory.exists()) directory.mkdir();

        // Load all menu files
        File[] yamlFiles = new File("plugins/" + plugin.getDescription().getName() + "/menus/").listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        if (yamlFiles == null) return;

        for (File file : yamlFiles) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            String name = (configuration.getString("identifier") != null ? configuration.getString("identifier") : file.getName().replace(".yml", ""));
            if (inventories.containsKey(name)) {
                plugin.getLogger().warning("Inventory with name '" + name + "' already exists, skipping duplicate..");
                continue;
            }

            CustomGUI customGUI;
            try {
                customGUI = new CustomGUI(plugin, YamlConfiguration.loadConfiguration(file));
            } catch (Exception e) {
                plugin.getLogger().severe("Could not load file '" + name + "' (YAML error).");
                e.printStackTrace();
                continue;
            }


            inventories.put(name, customGUI);
            plugin.getLogger().info("Loaded custom menu '" + name + "'.");
        }
    }

    public void addInventory(String key, AbstractInventory inventory) {
        inventories.put(key, inventory);
    }

    public Map<String, AbstractInventory> getInventories() {
        return inventories;
    }

    public AbstractInventory getInventory(String key) {
        return inventories.get(key);
    }

    public void onDisable() {
        inventories.values().forEach(abstractInventory -> {
            for (UUID uuid : abstractInventory.getOpenInventories()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) player.closeInventory();
            }
            abstractInventory.getOpenInventories().clear();
        });
        inventories.clear();
    }
}
