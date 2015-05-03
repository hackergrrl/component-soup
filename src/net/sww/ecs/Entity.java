package net.sww.ecs;

import java.util.*;

public class Entity {
    long id;

    World world;

    private Map<Class<? extends Component>, Component> components;

    private Entity parent;
    private List<Entity> children;

    public Entity(World world) {
        this.world = world;
        components = new HashMap<Class<? extends Component>, Component>();
        id = (new Random()).nextLong();
        parent = null;
        children = new LinkedList<Entity>();
    }

    public long getId() {
        return id;
    }

    public Entity install(Component component) {
        components.put(component.getClass(), component);
        component.installed(this);
        world.onComponentInstalled(this, component);
        return this;
    }

    public Entity uninstall(Component component) {
        components.remove(component.getClass());
        component.uninstalled(this);
        world.onComponentUninstalled(this, component);
        return this;
    }

    public void sendEvent(Event event) {
        for (Component component : components.values()) {
            component.onEvent(event);
        }
    }

    public void broadcastEvent(Event event) {
        for (Component component : components.values()) {
            component.onEvent(event);
        }
        for (Entity entity : children) {
            entity.broadcastEvent(event);
        }
    }

    public void update(float dt) {
        for (Component component : components.values()) {
            component.update(dt);
        }
    }

    public <T extends Component> T get(Class<T> type) {
        return type.cast(components.get(type));
    }

    public void addChild(Entity entity) {
        entity.abandonParent();
        entity.parent = this;
        children.add(entity);
    }

    public void setTag(String tag) {
        world.tag(this, tag);
    }

    private void abandonParent() {
        if (parent != null) {
            parent.removeChild(this);
            parent = null;
        }
    }

    private void removeChild(Entity entity) {
        children.remove(entity);
    }
}
