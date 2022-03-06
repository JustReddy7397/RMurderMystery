package ga.justreddy.wiki.rmurdermystery.arena.enums;

import wiki.justreddy.ga.reddyutils.uitl.ChatUtil;

public enum PlayerType implements ChatUtil {

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
        return c(name);
    }

    public String getJob() {
        return c(job);
    }
}
