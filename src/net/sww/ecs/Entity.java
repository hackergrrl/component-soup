package net.sww.ecs;

import java.util.*;

public class Entity {
    private long id;

    private World world;

    private Map<Class<? extends Component>, Component> components;

    public Entity(World world) {
        this.world = world;
        components = new HashMap<Class<? extends Component>, Component>();
        id = (new Random()).nextLong();
    }

    public long getId() {
        return id;
    }

    public void install(Component component) {
        components.put(component.getClass(), component);
        component.installed(this);
    }

    public void uninstall(Component component) {
        components.remove(component.getClass());
        component.uninstalled(this);
    }

    public void emit(Event event) {
        for (Component component : components.values()) {
            component.onEvent(event);
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
}
