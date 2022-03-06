package ga.justreddy.wiki.rmurdermystery.arena.player;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.Titles;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.enums.PlayerType;
import ga.justreddy.wiki.rmurdermystery.builder.ItemStackBuilder;
import ga.justreddy.wiki.rmurdermystery.cosmetics.PlayerCosmetics;
import ga.justreddy.wiki.rmurdermystery.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import wiki.justreddy.ga.reddyutils.uitl.ChatUtil;

import java.util.UUID;

public class GamePlayer implements ChatUtil {

    private final UUID uuid;
    private final Player player;
    private final String name;
    private ItemStack[] savedInventoryContents;
    private ItemStack[] savedArmorContents;
    private Arena arena;
    private PlayerData playerData;
    private PlayerType playerType;
    private final PlayerCosmetics playerCosmetics;
    private boolean dead;

    public GamePlayer(UUID uuid){
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
        this.name = this.player.getName();
        this.playerCosmetics = new PlayerCosmetics(this);
        this.playerData = new PlayerData(this);
        this.playerData.createPlayer();
        this.playerCosmetics.setKnifeSkinSelect(this.getPlayerData().getKnifeSkin());
        this.playerCosmetics.setVictoryDanceSelect(this.getPlayerData().getVictoryDance());
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation(){
        return player.getLocation();
    }

    public Arena getArena() {
        return arena;
    }

    public ItemStack[] getSavedArmorContents() {
        return savedArmorContents;
    }

    public void setSavedArmorContents(ItemStack[] savedArmorContents) {
        this.savedArmorContents = savedArmorContents;
    }

    public ItemStack[] getSavedInventoryContents() {
        return savedInventoryContents;
    }

    public void setSavedInventoryContents(ItemStack[] savedInventoryContents) {
        this.savedInventoryContents = savedInventoryContents;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public boolean isDead() {
        return dead;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public void sendMessage(String... message){
        if(message.length <= 0) return;
        for(String msg : message){
            ChatColor.translateAlternateColorCodes('&', msg);
            player.sendMessage(msg);
        }

    }

    public void giveItems(){
        ConfigurationSection configurationSection = MurderMystery.getPlugin(MurderMystery.class).getConfigManager().getFile("hotbar").getConfig().getConfigurationSection("items");
        if(configurationSection == null) return;
        for(String key : configurationSection.getKeys(false)){
            ConfigurationSection section = MurderMystery.getPlugin(MurderMystery.class).getConfigManager().getFile("hotbar").getConfig().getConfigurationSection("items." + key);
            ItemStackBuilder itemStackBuilder = new ItemStackBuilder(XMaterial.matchXMaterial(section.getString("material")).get().parseItem());
            if(section.getString("displayname") != null){
                if(player == null) itemStackBuilder.withName(c(section.getString("displayname")));
                else itemStackBuilder.withName(c(section.getString("displayname")), player);
            }

            if (section.getStringList("lore") != null && !section.getStringList("lore").isEmpty()) {
                if(player == null) itemStackBuilder.withLore(section.getStringList("lore"));
                else itemStackBuilder.withLore(section.getStringList("lore"), getPlayer());
            }

            getPlayer().getInventory().setItem(section.getInt("slot"), itemStackBuilder.build());

        }
    }

    public void sendMessage(String message){
        player.sendMessage(c(message));
    }

    public void setItem(int slot, ItemStack item){
         player.getInventory().setItem(slot, item);
    }

    public void sendTitle(String title, String subTitle){
        Titles.sendTitle(player, c(title), c(subTitle));
    }

    public void teleport(Location location){
        player.teleport(location);
    }

    public PlayerCosmetics getCosmetics() {
        return playerCosmetics;
    }
}
