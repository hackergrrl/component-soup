package net.sww.net.sww.ecs;

import java.util.List;
import java.util.Random;

public class Entity {
    private long id;
    private World world;
    private List<Component> components;

    public Entity(World world) {
        this.world = world;
        id = (new Random()).nextLong();
    }

    public long getId() {
        return id;
    }
}
