package net.sww.ecs;

import java.util.*;

public final class Entity {
    long id;

    int layers;

    World world;

    private Map<Class<? extends Component>, Component> components;

    private Entity parent;
    private List<Entity> children;

    Entity(World world) {
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
        Entity oldParent = entity.parent;
        if (oldParent != null) {
            oldParent.children.remove(this);
        }
        entity.parent = this;
        children.add(entity);
//            this.sendEvent();
        entity.sendEvent(new NewParent(oldParent, this));
    }

    public void abandonParent() {
        if (parent != null) {
            Entity oldParent = parent;
            parent.children.remove(this);
            parent = null;
//            oldParent.sendEvent();
            this.sendEvent(new NewParent(oldParent, null));
        }
    }

    public void removeChild(Entity entity) {
        if (entity.parent != this) {
            throw new IllegalArgumentException("cannot remove entity that is not a child of parent");
        }
        entity.abandonParent();
    }

    public void setTag(String tag) {
        world.tag(this, tag);
    }

    public Entity getParent() {
        return parent;
    }

    public void addToLayers(int layers) {
        this.layers |= layers;
    }

    public void removeFromLayers(int layers) {
        this.layers &= (~layers);
    }

    public boolean isInLayers(int layers) {
        return (this.layers & layers) != 0;
    }
}
