package ga.justreddy.wiki.rmurdermystery.arena.enums;


import ga.justreddy.wiki.rmurdermystery.utils.Utils;

public enum PlayerType {

    INNOCENT("&aInnocent", "&aSurvive"),
    DETECTIVE("&bDetective", "&bKill the murderer"),
    MURDERER("&cMurderer", "&cKill every player"),
    DEAD("&7Dead", "&cIdk do something");

    private final String name;
    private final String job;

    PlayerType(String name, String job){
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return Utils.format(name);
    }

    public String getJob() {
        return Utils.format(job);
    }
}
