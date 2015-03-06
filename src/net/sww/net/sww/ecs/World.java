package net.sww.net.sww.ecs;

import java.util.HashMap;
import java.util.Map;

public class World {
    Map<Long, Entity> entities;

    public World() {
        entities = new HashMap<Long, Entity>();
    }

    public Entity createEntity() {
        Entity entity = new Entity(this);
        entities.put(entity.getId(), entity);
        return entity;
    }
}
