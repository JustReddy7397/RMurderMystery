package ga.justreddy.wiki.rmurdermystery.cosmetics;

import de.tr7zw.changeme.nbtapi.NBTEntity;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class LastWords extends Cosmetics {

    private final List<String> lastWords = new ArrayList<>();

    public LastWords(String name, int id, int cost, String permission, String... lastWords) {
        super(name, id, cost, permission);
        if(lastWords == null) throw new ArrayIndexOutOfBoundsException("There are no last words in the cosmetic " + name);
        this.lastWords.addAll(Arrays.asList(lastWords));
    }

    private ArmorStand player;
    private ArmorStand playerLastWords;

    public void spawn(GamePlayer gamePlayer){

        Random random = new Random();

        String line = lastWords.get(random.nextInt(lastWords.size()));

        player = gamePlayer.getLocation().getWorld().spawn(gamePlayer.getLocation(), ArmorStand.class);
        NBTEntity entity = new NBTEntity(player);
        entity.setInteger("Invulnerable", 1);
        player.setCustomName(gamePlayer.c("&6" + gamePlayer.getName() + "'s last words:"));
        player.setCustomNameVisible(true);
        player.setVisible(false);
        player.setGravity(false);
        playerLastWords = gamePlayer.getLocation().getWorld().spawn(gamePlayer.getLocation().subtract(0, 0.40, 0), ArmorStand.class);
        NBTEntity asd = new NBTEntity(playerLastWords);
        asd.setInteger("Invulnerable", 1);
        playerLastWords.setGravity(false);
        playerLastWords.setCustomName(gamePlayer.c("&f&o\"" + line + "\""));
        playerLastWords.setCustomNameVisible(true);
        playerLastWords.setVisible(false);
    }

    public void remove(){
        player.remove();
        playerLastWords.remove();
    }

    public List<String> getLastWords() {
        return lastWords;
    }
}
