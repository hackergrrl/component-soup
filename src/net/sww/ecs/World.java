package net.sww.ecs;

import java.util.HashMap;
import java.util.Map;

public class World {
    private Map<Long, Entity> entities;
    private Map<String, Entity> tags;

    public World() {
        entities = new HashMap<Long, Entity>();
        tags = new HashMap<String, Entity>();
    }

    public Entity createEntity() {
        Entity entity = new Entity(this);
        entities.put(entity.getId(), entity);
        return entity;
    }

    public void update(float dt) {
        for (Entity entity : entities.values()) {
            entity.update(dt);
        }
    }

    void tag(Entity entity, String tag) {
        tags.put(tag, entity);
    }

    public Entity getByTag(String tag) {
        return tags.get(tag);
    }
}
