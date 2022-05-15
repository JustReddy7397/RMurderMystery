package ga.justreddy.wiki.rmurdermystery.arena.enums;


import ga.justreddy.wiki.rmurdermystery.utils.Utils;

public enum SignState  {

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
        return Utils.format(identifier);
    }
}
