package ga.justreddy.wiki.rmurdermystery;

import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import ga.justreddy.wiki.rmurdermystery.arena.player.PlayerController;
import ga.justreddy.wiki.rmurdermystery.controller.KnifeSkinsController;
import ga.justreddy.wiki.rmurdermystery.controller.VictoryDancesController;
import ga.justreddy.wiki.rmurdermystery.cosmetics.VictoryDances;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPI extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "RMurderMystery";
    }

    @Override
    public @NotNull String getAuthor() {
        return "JustReddy";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";
        GamePlayer gamePlayer = PlayerController.getPlayerController().get(player.getUniqueId());
        if (gamePlayer == null) return "";

        switch (params) {
            case "gamesplayed":
                return String.valueOf(gamePlayer.getPlayerData().getGamesPlayed());
            case "gameswon_murderer":
                return String.valueOf(gamePlayer.getPlayerData().getGamesWonMurderer());
            case "gameswon_detective":
                return String.valueOf(gamePlayer.getPlayerData().getGamesWonDetective());
            case "gameswon_innocent":
                return String.valueOf(gamePlayer.getPlayerData().getGamesWonInnocent());
            case "gameswon_total":
                return String.valueOf(gamePlayer.getPlayerData().getGamesWonTotal());
            case "playerskilled_murderer":
                return String.valueOf(gamePlayer.getPlayerData().getPlayersKilledMurderer());
            case "playerskilled_detective":
                return String.valueOf(gamePlayer.getPlayerData().getPlayersKilledDetective());
            case "playerskilled_innocent":
                return String.valueOf(gamePlayer.getPlayerData().getPlayersKilledInnocent());
            case "playerskilled_total":
                return String.valueOf(gamePlayer.getPlayerData().getPlayersKilledTotal());
            case "victorydance_id":
                return String.valueOf(gamePlayer.getPlayerData().getVictoryDance());
            case "victorydance_name":
                return VictoryDancesController.getVictoryDancesController().getByGamePlayer(gamePlayer).getName();
            case "knifeskin_id":
                return String.valueOf(gamePlayer.getPlayerData().getKnifeSkin());
            case "knifeskin_name":
                return KnifeSkinsController.getKnifeSkinsController().getByGamePlayer(gamePlayer).getName();
        }

        return null;
    }
}
