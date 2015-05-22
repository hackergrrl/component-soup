package net.sww.ecs;

import java.util.*;

public final class Entity {
    long id;

    int layers;

    World world;

    boolean componentsLocked = false;
    Map<Class<? extends Component>, Component> components;

    List<Component> toAdd, toRemove;

    Entity parent;
    List<Entity> children;

    Entity(World world) {
        this.world = world;
        components = new HashMap<Class<? extends Component>, Component>();
        id = (new Random()).nextLong();
        parent = null;
        children = new LinkedList<Entity>();
        toAdd = new LinkedList<Component>();
        toRemove = new LinkedList<Component>();
    }

    public long getId() {
        return id;
    }

    public Iterator<Entity> getChildren() {
        return children.listIterator();
    }

    public <T extends Component> List<T> getChildrenWithComponent(Class<T> type) {
        List<T> results = new LinkedList<T>();
        for (Entity child : children) {
            T component = child.get(type);
            if (component != null) {
                results.add(component);
            }
        }
        return results;
    }

    public Entity install(Component component) {
        return _install(component);
    }

    public Entity uninstall(Component component) {
        return _uninstall(component);
    }

    public void sendEvent(Event event) {
        componentsLocked = true;
        for (Component component : components.values()) {
            component.onEvent(event);
        }
        componentsLocked = false;
    }

    public void broadcastEvent(Event event) {
        componentsLocked = true;
        for (Component component : components.values()) {
            component.onEvent(event);
        }
        componentsLocked = false;
        for (Entity entity : children) {
            entity.broadcastEvent(event);
        }
    }

    public void update(float dt) {
        componentsLocked = true;
        for (Component component : components.values()) {
            component.update(dt);
        }
        componentsLocked = false;

        for (Component component : toAdd) {
            _install(component);
        }
        toAdd.clear();

        for (Component component : toRemove) {
            _uninstall(component);
        }
        toRemove.clear();
    }

    public <T extends Component> T get(Class<T> type) {
        return type.cast(components.get(type));
    }

    public <T extends Component> boolean has(Class<T> type) {
        return get(type) != null;
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

    private Entity _install(Component component) {
        if (componentsLocked) {
            toAdd.add(component);
        } else {
            components.put(component.getClass(), component);
            component.installed(this);
            world.onComponentInstalled(this, component);
        }
        return this;
    }

    private Entity _uninstall(Component component) {
        if (componentsLocked) {
            toRemove.add(component);
        } else {
            components.remove(component.getClass());
            component.uninstalled(this);
            world.onComponentUninstalled(this, component);
        }
        return this;
    }
}
