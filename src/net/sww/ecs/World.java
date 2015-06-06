package net.sww.ecs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class World {
    private Map<Long, Entity> entities;
    private Map<String, Entity> tags;
    private List<Manager> managers;
    private List<Entity> toAdd;
    private List<Entity> toRemove;

    public World() {
        entities = new HashMap<Long, Entity>();
        tags = new HashMap<String, Entity>();
        managers = new LinkedList<Manager>();
        toAdd = new LinkedList<Entity>();
        toRemove = new LinkedList<Entity>();
    }

    public Entity createEntity() {
        Entity entity = new Entity(this);
        toAdd.add(entity);

        for (Manager manager : managers) {
            manager.onEntityAdded(entity);
        }

        return entity;
    }

    public void removeEntity(Entity entity) {
        for (Entity child : entity.children) {
            removeEntity(child);
        }
        toRemove.add(entity);
    }

    public void update(float dt) {
        for (Entity entity : toAdd) {
            entities.put(entity.getId(), entity);
        }
        toAdd.clear();

        for (Entity entity : entities.values()) {
            entity.update(dt);
        }

        for (Entity entity : toRemove) {
            for (Manager manager : managers) {
                for (Component component : entity.components.values()) {
                    manager.onComponentUninstalled(entity, component);
                }
                manager.onEntityRemoved(entity);
            }

            entities.remove(entity.getId());
        }
        toRemove.clear();
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

    public List<Entity> queryAllInLayers(int layers) {
        LinkedList<Entity> results = new LinkedList<Entity>();
        for (Entity entity : entities.values()) {
            if (entity.isInLayers(layers)) {
                results.add(entity);
            }
        }
        return results;
    }
}