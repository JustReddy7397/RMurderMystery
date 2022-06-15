package ga.justreddy.wiki.rmurdermystery.arena.player;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.Titles;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.enums.PlayerType;
import ga.justreddy.wiki.rmurdermystery.builder.ItemStackBuilder;
import ga.justreddy.wiki.rmurdermystery.cosmetics.PlayerCosmetics;
import ga.justreddy.wiki.rmurdermystery.data.PlayerData;
import ga.justreddy.wiki.rmurdermystery.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class GamePlayer  {

    private final UUID uuid;
    private final Player player;
    private final String name;
    private ItemStack[] savedInventoryContents;
    private ItemStack[] savedArmorContents;
    private Arena arena;
    private final PlayerData playerData;
    private PlayerType playerType;
    private final PlayerCosmetics playerCosmetics;
    private boolean dead;
    private ArmorStand nameTagHider;

    public GamePlayer(UUID uuid){
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
        this.name = this.player.getName();
        this.playerData = new PlayerData(this);
        this.playerData.createPlayer();
        this.playerCosmetics = new PlayerCosmetics(this);
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
        ConfigurationSection configurationSection = MurderMystery.getPlugin(MurderMystery.class).getHotbarConfig().getConfig().getConfigurationSection("items");
        if(configurationSection == null) return;
        for(String key : configurationSection.getKeys(false)){
            ConfigurationSection section = MurderMystery.getPlugin(MurderMystery.class).getHotbarConfig().getConfig().getConfigurationSection("items." + key);
            ItemStackBuilder itemStackBuilder = new ItemStackBuilder(XMaterial.matchXMaterial(section.getString("material")).get().parseItem());
            if(section.getString("displayname") != null){
                if(player == null) itemStackBuilder.withName(Utils.format(section.getString("displayname")));
                else itemStackBuilder.withName(Utils.format(section.getString("displayname")), player);
            }

            if (section.getStringList("lore") != null && !section.getStringList("lore").isEmpty()) {
                if(player == null) itemStackBuilder.withLore(section.getStringList("lore"));
                else itemStackBuilder.withLore(section.getStringList("lore"), getPlayer());
            }

            getPlayer().getInventory().setItem(section.getInt("slot"), itemStackBuilder.build());

        }
    }

    public void hideNameTag() {
        nameTagHider = (ArmorStand) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ARMOR_STAND);
        NBTEntity entity = new NBTEntity(nameTagHider);
        entity.setInteger("Invulnerable", 1);
        nameTagHider.setMarker(true);
        nameTagHider.setVisible(false);
        player.setPassenger(nameTagHider);

/*        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("nametagHide");
        if (team == null) {
            team = scoreboard.registerNewTeam("nametagHide");
            team.setNameTagVisibility(NameTagVisibility.NEVER);
        }
        System.out.println(team + " team");
        team.addEntry(getName());
        System.out.println(team.getNameTagVisibility() + " nametag");
        System.out.println(team.getEntries() + " entries");
        System.out.println(scoreboard.getTeams() + " teams");*/
/*        nameTagHider = player.getWorld().spawn(getLocation(), ArmorStand.class);
        nameTagHider.setMarker(true);
        NBTEntity entity = new NBTEntity(nameTagHider);
        entity.setInteger("Invulnerable", 1);
        entity.setInteger("Invisible", 1);*/
    }

    public void resetNameTag() {
        if (nameTagHider != null) nameTagHider.remove();
    }

    public void sendMessage(String message){
        player.sendMessage(Utils.format(message));
    }

    public void setItem(int slot, ItemStack item){
        player.getInventory().setItem(slot, item);
    }

    public void sendTitle(String title, String subTitle){
        Titles.sendTitle(player, Utils.format(title), Utils.format(subTitle));
    }

    public void teleport(Location location){
        player.teleport(location);
    }

    public PlayerCosmetics getCosmetics() {
        return playerCosmetics;
    }


}
