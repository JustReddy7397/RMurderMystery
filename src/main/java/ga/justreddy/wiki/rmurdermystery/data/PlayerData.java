package ga.justreddy.wiki.rmurdermystery.data;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.arena.player.GamePlayer;
import org.bson.Document;

public class PlayerData {

    private final MurderMystery plugin = MurderMystery.getPlugin(MurderMystery.class);

    // TODO make playerData

    private final GamePlayer gamePlayer;

    public PlayerData(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }

    public void createPlayer(){
        if(plugin.isMongoConnected()){
            if(plugin.getMongoBuilder().getDatabase("stats").find(new Document("UUID", gamePlayer.getUuid())).first() != null) return;
            Document document = new Document("UUID", gamePlayer.getUuid())
                    .append("gamesPlayed", 0)
                    .append("gamesWonMurderer", 0)
                    .append("gamesWonDetective", 0)
                    .append("gamesWonInnocent", 0)
                    .append("playersKilledMurderer", 0)
                    .append("playersKilledDetective", 0)
                    .append("playersKilledInnocent", 0)
                    .append("knifeskin", 0)
                    .append("victorydance", 0);

            plugin.getMongoBuilder().getDatabase("stats").insertOne(document);
        }
    }

    public int getGamesPlayed(){
        if(plugin.isMongoConnected()){
            Document document = plugin.getMongoBuilder().getDatabase("stats").find(new Document("UUID", gamePlayer.getUuid())).first();
            if(document != null){
                return document.getInteger("gamesPlayed");
            }
        }
        return 0;
    }

    public void setGamesPlayed(int amount){
        if(plugin.isMongoConnected()){
            Document document = plugin.getMongoBuilder().getDatabase("stats").find(new Document("UUID", gamePlayer.getUuid())).first();
            if(document != null){
                plugin.getMongoBuilder().getDatabase("stats").updateOne(Filters.eq("UUID", gamePlayer.getUuid()), Updates.set("gamesPlayed", (getGamesPlayed() + amount)));
            }
        }
    }

    public int getGamesWonMurderer(){
        if(plugin.isMongoConnected()){
            Document document = plugin.getMongoBuilder().getDatabase("stats").find(new Document("UUID", gamePlayer.getUuid())).first();
            if(document != null){
                return document.getInteger("gamesWonMurderer");
            }
        }
        return 0;
    }

    public int getGamesWonDetective(){
        if(plugin.isMongoConnected()){
            Document document = plugin.getMongoBuilder().getDatabase("stats").find(new Document("UUID", gamePlayer.getUuid())).first();
            if(document != null){
                return document.getInteger("gamesWonDetective");
            }
        }
        return 0;
    }

    public int getGamesWonInnocent(){
        if(plugin.isMongoConnected()){
            Document document = plugin.getMongoBuilder().getDatabase("stats").find(new Document("UUID", gamePlayer.getUuid())).first();
            if(document != null){
                return document.getInteger("gamesWonInnocent");
            }
        }
        return 0;
    }

    public int getPlayersKilledMurderer(){
        if(plugin.isMongoConnected()){
            Document document = plugin.getMongoBuilder().getDatabase("stats").find(new Document("UUID", gamePlayer.getUuid())).first();
            if(document != null){
                return document.getInteger("playersKilledMurderer");
            }
        }
        return 0;
    }

    public int getPlayersKilledDetective(){
        if(plugin.isMongoConnected()){
            Document document = plugin.getMongoBuilder().getDatabase("stats").find(new Document("UUID", gamePlayer.getUuid())).first();
            if(document != null){
                return document.getInteger("playersKilledDetective");
            }
        }
        return 0;
    }

    public int getPlayersKilledInnocent(){
        if(plugin.isMongoConnected()){
            Document document = plugin.getMongoBuilder().getDatabase("stats").find(new Document("UUID", gamePlayer.getUuid())).first();
            if(document != null){
                return document.getInteger("playersKilledInnocent");
            }
        }
        return 0;
    }

    public int getPlayersKilledTotal(){
        return (getPlayersKilledMurderer() + getPlayersKilledInnocent() + getPlayersKilledDetective());
    }

    public int getGamesWonTotal() {
        return (getGamesWonDetective() + getGamesWonInnocent() + getGamesWonMurderer());
    }

    public int getKnifeSkin(){
        if(plugin.isMongoConnected()){
            Document document = plugin.getMongoBuilder().getDatabase("stats").find(new Document("UUID", gamePlayer.getUuid())).first();
            if(document != null){
                return document.getInteger("knifeskin");
            }
        }
        return 0;
    }

    public void setKnifeSkin(int id){
        if(plugin.isMongoConnected()){
            Document document = plugin.getMongoBuilder().getDatabase("stats").find(new Document("UUID", gamePlayer.getUuid())).first();
            if(document != null){
                plugin.getMongoBuilder().getDatabase("stats").updateOne(Filters.eq("UUID", gamePlayer.getUuid()), Updates.set("knifeskin", id));
            }
        }
    }


    public int getVictoryDance(){
        if(plugin.isMongoConnected()){
            Document document = plugin.getMongoBuilder().getDatabase("stats").find(new Document("UUID", gamePlayer.getUuid())).first();
            if(document != null){
                return document.getInteger("victorydance");
            }
        }
        return 0;
    }

    public void setVictoryDance(int id){
        if(plugin.isMongoConnected()){
            Document document = plugin.getMongoBuilder().getDatabase("stats").find(new Document("UUID", gamePlayer.getUuid())).first();
            if(document != null){
                plugin.getMongoBuilder().getDatabase("stats").updateOne(Filters.eq("UUID", gamePlayer.getUuid()), Updates.set("victorydance", id));
            }
        }
    }

}
