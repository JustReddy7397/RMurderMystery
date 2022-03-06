package ga.justreddy.wiki.rmurdermystery.exceptions;

public class MissingArenaObjectsException extends RuntimeException {

    public MissingArenaObjectsException(String name){
        super("The arena " + name + " is missing objects.");
    }

}
