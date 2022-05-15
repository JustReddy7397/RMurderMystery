package ga.justreddy.wiki.rmurdermystery.builder;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackBuilder {

    private final ItemStack ITEM_STACK;

    private static final MurderMystery PLUGIN = MurderMystery.getPlugin(MurderMystery.class);
    public ItemStackBuilder(ItemStack item) {
        this.ITEM_STACK = item;
    }

    public static ItemStackBuilder getItemStack(ConfigurationSection section, Player player) {
        ItemStack item = XMaterial.matchXMaterial(section.getString("material").toUpperCase()).get().parseItem();

        ItemStackBuilder builder = new ItemStackBuilder(item);

        if (section.contains("amount")) {
            builder.withAmount(section.getInt("amount"));
        }

        if (section.contains("username") && player != null) {
            builder.setSkullOwner(section.getString("username").replace("%player%", player.getName()));
        }

        if (section.contains("display_name")) {
            if (player != null) builder.withName(section.getString("display_name"), player);
            else builder.withName(section.getString("display_name"));
        }

        if (section.contains("lore")) {
            if (player != null) builder.withLore(section.getStringList("lore"), player);
            else builder.withLore(section.getStringList("lore"));
        }

        if (section.contains("glow") && section.getBoolean("glow")) {
            builder.withGlow();
        }

        if (section.contains("item_flags")) {
            List<ItemFlag> flags = new ArrayList<>();
            section.getStringList("item_flags").forEach(text -> {
                try {
                    ItemFlag flag = ItemFlag.valueOf(text);
                    flags.add(flag);
                } catch (IllegalArgumentException ignored) {
                }
            });
            builder.withFlags(flags.toArray(new ItemFlag[0]));
        }

        return builder;
    }

    public static ItemStackBuilder getItemStack(ConfigurationSection section) {
        return getItemStack(section, null);
    }

    public ItemStackBuilder withAmount(int amount) {
        ITEM_STACK.setAmount(amount);
        return this;
    }

    public ItemStackBuilder withFlags(ItemFlag... flags) {
        ItemMeta meta = ITEM_STACK.getItemMeta();
        meta.addItemFlags(flags);
        ITEM_STACK.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder withName(String name) {
        final ItemMeta meta = ITEM_STACK.getItemMeta();
        meta.setDisplayName(Utils.format(name));
        ITEM_STACK.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder withName(String name, Player player) {
        final ItemMeta meta = ITEM_STACK.getItemMeta();
        if(!MurderMystery.PAPI) meta.setDisplayName(Utils.format(name));
        else meta.setDisplayName(Utils.format(PlaceholderAPI.setPlaceholders(player, name)));
        ITEM_STACK.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) ITEM_STACK.getItemMeta();
            im.setOwner(owner);
            ITEM_STACK.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public ItemStackBuilder withLore(List<String> lore, Player player) {
        final ItemMeta meta = ITEM_STACK.getItemMeta();
        List<String> coloredLore = new ArrayList<>();
        for (String s : lore) {
            if(MurderMystery.PAPI) s = PlaceholderAPI.setPlaceholders(player, s);
            coloredLore.add(Utils.format(s));
        }
        meta.setLore(coloredLore);
        ITEM_STACK.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder withLore(List<String> lore) {
        final ItemMeta meta = ITEM_STACK.getItemMeta();
        List<String> coloredLore = new ArrayList<String>();
        for (String s : lore) {
            coloredLore.add(Utils.format(s));
        }
        meta.setLore(coloredLore);
        ITEM_STACK.setItemMeta(meta);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemStackBuilder withDurability(int durability) {
        ITEM_STACK.setDurability((short) durability);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemStackBuilder withData(int data) {
        ITEM_STACK.setDurability((short) data);
        return this;
    }

    public ItemStackBuilder withEnchantment(Enchantment enchantment, final int level) {
        ITEM_STACK.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemStackBuilder withEnchantment(Enchantment enchantment) {
        ITEM_STACK.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemStackBuilder withGlow() {
        final ItemMeta meta = ITEM_STACK.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ITEM_STACK.setItemMeta(meta);
        ITEM_STACK.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        return this;
    }

    public ItemStackBuilder withType(Material material) {
        ITEM_STACK.setType(material);
        return this;
    }

    public ItemStackBuilder clearLore() {
        final ItemMeta meta = ITEM_STACK.getItemMeta();
        meta.setLore(new ArrayList<String>());
        ITEM_STACK.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder clearEnchantments() {
        for (Enchantment enchantment : ITEM_STACK.getEnchantments().keySet()) {
            ITEM_STACK.removeEnchantment(enchantment);
        }
        return this;
    }

    public ItemStackBuilder withColor(Color color) {
        Material type = ITEM_STACK.getType();
        if (type == Material.LEATHER_BOOTS || type == Material.LEATHER_CHESTPLATE || type == Material.LEATHER_HELMET || type == Material.LEATHER_LEGGINGS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) ITEM_STACK.getItemMeta();
            meta.setColor(color);
            ITEM_STACK.setItemMeta(meta);
            return this;
        } else {
            throw new IllegalArgumentException("withColor is only applicable for leather armor!");
        }
    }

    public ItemStack build() {
        return ITEM_STACK;
    }

}
