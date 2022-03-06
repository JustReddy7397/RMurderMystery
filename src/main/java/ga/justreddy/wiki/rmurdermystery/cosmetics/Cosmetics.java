package ga.justreddy.wiki.rmurdermystery.cosmetics;

public abstract class Cosmetics {

    private final String name;
    private final int id;
    private final int cost;
    private final String permission;

    public Cosmetics(String name, int id, int cost, String permission){
        this.name = name;
        this.id = id;
        this.cost = cost;
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getId() {
        return id;
    }
}
