package ga.justreddy.wiki.rmurdermystery.cosmetics.victorydances;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.cosmetics.VictoryDances;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class FireworkDance extends VictoryDances {


    public FireworkDance() {
        super("Fireworks", 0, 300, "mm.cosmetics.dances.firework");
    }

    private BukkitTask bukkitTask;

    @Override
    public void start(GamePlayer gamePlayer) {
        bukkitTask = new BukkitRunnable(){
            @Override
            public void run() {
                Firework firework = gamePlayer.getLocation().getWorld().spawn(gamePlayer.getLocation(), Firework.class);
                FireworkMeta fireworkMeta = firework.getFireworkMeta();
                fireworkMeta.addEffect(FireworkEffect.builder()
                        .flicker(true)
                        .trail(true)
                        .with(FireworkEffect.Type.BURST)
                        .withColor(Color.YELLOW, Color.BLUE, Color.RED)
                        .build());
                fireworkMeta.setPower(0);
                firework.setFireworkMeta(fireworkMeta);

                Bukkit.getScheduler().runTaskLater(MurderMystery.getPlugin(MurderMystery.class), firework::detonate, 15L);

            }
        }.runTaskTimer(MurderMystery.getPlugin(MurderMystery.class), 0, 20L);
    }

    @Override
    public void stop(GamePlayer gamePlayer) {
        bukkitTask.cancel();
        System.out.println("Stopped");
    }
}
