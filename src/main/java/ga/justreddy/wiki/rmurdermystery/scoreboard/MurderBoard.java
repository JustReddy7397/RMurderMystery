package ga.justreddy.wiki.rmurdermystery.scoreboard;

import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.Arena;
import ga.justreddy.wiki.rmurdermystery.arena.ArenaManager;
import ga.justreddy.wiki.rmurdermystery.arena.enums.GameState;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.player.PlayerController;
import ga.justreddy.wiki.rmurdermystery.builder.PlaceholderBuilder;
import ga.justreddy.wiki.rmurdermystery.scoreboard.lib.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class MurderBoard implements AssembleAdapter {
    @Override
    public String getTitle(Player player) {
        try {
            GamePlayer gamePlayer = PlayerController.getPlayerController().get(player.getUniqueId());
            if (ArenaManager.getArenaManager().getArenas().size() == 0) {
                return PlaceholderBuilder.setPlaceholders(
                        MurderMystery.getPlugin(MurderMystery.class).getScoreboardConfig().getConfig().getString("lobby.title"),
                        gamePlayer
                );
            }
            for (Arena arena : ArenaManager.getArenaManager().getArenas()) {
                if (!arena.getPlayers().contains(gamePlayer)) {
                    return PlaceholderBuilder.setPlaceholders(
                            MurderMystery.getPlugin(MurderMystery.class).getScoreboardConfig().getConfig().getString("lobby.title"),
                            gamePlayer
                    );
                } else {
                    if (arena.getGameState() == GameState.LOBBY) {
                        return PlaceholderBuilder.setPlaceholders(
                                MurderMystery.getPlugin(MurderMystery.class).getScoreboardConfig().getConfig().getString("game.lobby.title"),
                                arena, gamePlayer
                        );
                    } else if (arena.getGameState() == GameState.WAITING) {
                        return PlaceholderBuilder.setPlaceholders(
                                MurderMystery.getPlugin(MurderMystery.class).getScoreboardConfig().getConfig().getString("game.waiting.title"),
                                arena, gamePlayer
                        );
                    } else {
                        return PlaceholderBuilder.setPlaceholders(
                                MurderMystery.getPlugin(MurderMystery.class).getScoreboardConfig().getConfig().getString("game.playing.title"),
                                arena, gamePlayer
                        );
                    }
                }
            }
        }catch(NullPointerException ignored ){}
        return null;
    }

    @Override
    public List<String> getLines(Player player) {
        try{
            GamePlayer gamePlayer = PlayerController.getPlayerController().get(player.getUniqueId());
            for(Arena arena : ArenaManager.getArenaManager().getArenas()){
                if (!arena.getPlayers().contains(gamePlayer)) {
                    return MurderMystery.getPlugin(MurderMystery.class).getScoreboardConfig().getConfig().getStringList("lobby.lines")
                            .stream().map(s -> PlaceholderBuilder.setPlaceholders(s, gamePlayer)).collect(Collectors.toList());
                }else{
                    if(arena.getGameState() == GameState.LOBBY){
                        return MurderMystery.getPlugin(MurderMystery.class).getScoreboardConfig().getConfig().getStringList("game.lobby.lines")
                                .stream().map(s -> PlaceholderBuilder.setPlaceholders(s, arena, gamePlayer)).collect(Collectors.toList());

                    }else if(arena.getGameState() == GameState.WAITING){
                        return MurderMystery.getPlugin(MurderMystery.class).getScoreboardConfig().getConfig().getStringList("game.waiting.lines")
                                .stream().map(s -> PlaceholderBuilder.setPlaceholders(s, arena, gamePlayer)).collect(Collectors.toList());

                    }else{
                        return MurderMystery.getPlugin(MurderMystery.class).getScoreboardConfig().getConfig().getStringList("game.playing.lines")
                                .stream().map(s -> PlaceholderBuilder.setPlaceholders(s, arena, gamePlayer)).collect(Collectors.toList());

                    }
                }
            }
        }catch (NullPointerException ignored) {}
        return null;
    }
}
