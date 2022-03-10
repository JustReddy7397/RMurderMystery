package ga.justreddy.wiki.rmurdermystery.arena.tasks;

import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class GoldTask extends BukkitRunnable {

    private final Arena arena;

    public GoldTask(Arena arena){
        this.arena = arena;
    }

    int time = 15;

    @Override
    public void run() {
        if(arena.getGameState() == GameState.PLAYING){
            time--;
        }

        if(time <= 0){
            time = 15;
            Random random = new Random();
            if(arena.getGoldLocations().size() == 0) return;
            if(arena.getGoldLocations().get(random.nextInt(arena.getGoldLocations().size())).getWorld().getEntities().size() >= 1) return;
            arena.getGoldLocations().get(random.nextInt(arena.getGoldLocations().size())).getWorld().dropItemNaturally(
                    arena.getGoldLocations().get(random.nextInt(arena.getGoldLocations().size())),
                      new ItemStack(Material.GOLD_INGOT)
            );
        }
    }
}
