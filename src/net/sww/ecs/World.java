package net.sww.ecs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class World {
    private Map<Long, Entity> entities;
    private Map<String, Entity> tags;
    private List<Manager> managers;

    public World() {
        entities = new HashMap<Long, Entity>();
        tags = new HashMap<String, Entity>();
        managers = new LinkedList<Manager>();
    }

    public Entity createEntity() {
        Entity entity = new Entity(this);
        entities.put(entity.getId(), entity);

        for (Manager manager : managers) {
            manager.onEntityAdded(entity);
        }

        return entity;
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity.getId());

        for (Manager manager : managers) {
            manager.onEntityRemoved(entity);
        }
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

    public void installManager(Manager manager) {
        managers.add(manager);
        manager.world = this;
    }

    public void onComponentInstalled(Entity entity, Component component) {
        for (Manager manager : managers) {
            manager.onComponentInstalled(entity, component);
        }
    }

    public void onComponentUninstalled(Entity entity, Component component) {
        for (Manager manager : managers) {
            manager.onComponentUninstalled(entity, component);
        }
    }
}
