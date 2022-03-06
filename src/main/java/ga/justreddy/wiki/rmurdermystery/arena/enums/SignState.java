package ga.justreddy.wiki.rmurdermystery.arena.enums;

import wiki.justreddy.ga.reddyutils.uitl.ChatUtil;

public enum SignState implements ChatUtil {

    WAITING("&aWaiting"),
    STARTING("&6Starting"),
    FULL("&cFull"),
    PLAYING("&cPlaying"),
    ENDING("&7Ending"),
    RESTARTING("&9Restarting");

    private final String identifier;

    SignState(String identifier){
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return c(identifier);
    }
}
