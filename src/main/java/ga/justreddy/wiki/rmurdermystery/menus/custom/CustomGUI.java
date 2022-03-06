package ga.justreddy.wiki.rmurdermystery.menus.custom;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.menus.AbstractInventory;
import ga.justreddy.wiki.rmurdermystery.menus.InventoryBuilder;
import ga.justreddy.wiki.rmurdermystery.menus.InventoryItem;
import ga.justreddy.wiki.rmurdermystery.builder.ItemStackBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import wiki.justreddy.ga.reddyutils.uitl.ChatUtil;

public class CustomGUI extends AbstractInventory implements ChatUtil {

    private InventoryBuilder inventory;
    private FileConfiguration config;

    public CustomGUI(MurderMystery plugin, FileConfiguration config) {
        super(plugin);
        this.config = config;
    }

    @Override
    public void onEnable() {

        InventoryBuilder inventoryBuilder = new InventoryBuilder(config.getInt("size"), c(config.getString("menu_name")));

        if (config.contains("refresh") && config.getBoolean("refresh.enabled")) {
            setInventoryRefresh(config.getLong("refresh.rate"));
        }

        for (String entry : config.getConfigurationSection("items").getKeys(false)) {

            try {
                ItemStackBuilder builder = ItemStackBuilder.getItemStack(config.getConfigurationSection("items." + entry));

                InventoryItem inventoryItem;
                if (!config.contains("items." + entry + ".actions")) {
                    inventoryItem = new InventoryItem(builder.build());
                } else {
                    inventoryItem = new InventoryItem(builder.build()).addClickAction(p -> getPlugin().getActionManager().executeActions(p, config.getStringList("items." + entry + ".actions")));
                }

                if (config.contains("items." + entry + ".slots")) {
                    for (String slot : config.getStringList("items." + entry + ".slots")) {
                        inventoryBuilder.setItem(Integer.parseInt(slot), inventoryItem);
                    }
                } else if (config.contains("items." + entry + ".slot")) {
                    int slot = config.getInt("items." + entry + ".slot");
                    if (slot == -1) {
                        while (inventoryBuilder.getInventory().firstEmpty() != -1) {
                            inventoryBuilder.setItem(inventoryBuilder.getInventory().firstEmpty(), inventoryItem);
                        }
                    } else inventoryBuilder.setItem(slot, inventoryItem);
                }

            } catch (Exception e) {
                e.printStackTrace();
                getPlugin().getLogger().warning("There was an error loading GUI item ID '" + entry + "', skipping..");
            }
        }

        inventory = inventoryBuilder;

    }

    @Override
    protected Inventory getInventory() {
        return inventory.getInventory();
    }


    public FileConfiguration getConfig() {
        return config;
    }
}
